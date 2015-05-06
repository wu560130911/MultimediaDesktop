/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wms.studio.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.json.JSON;
import com.wms.studio.annotations.HandlerPoint;
import com.wms.studio.api.constant.EmailConstant;
import com.wms.studio.api.constant.LoginType;
import com.wms.studio.api.constant.UserConstant;
import com.wms.studio.api.constant.UserDisableReason;
import com.wms.studio.api.constant.UserRole;
import com.wms.studio.api.dto.CommonResponseDto;
import com.wms.studio.api.dto.MailMessage;
import com.wms.studio.api.dto.PageDto;
import com.wms.studio.api.dto.WallpaperDto;
import com.wms.studio.api.dto.page.PageSize;
import com.wms.studio.api.dto.sort.SortDto;
import com.wms.studio.api.dto.user.CheckEmailDto;
import com.wms.studio.api.dto.user.UpdateUserDto;
import com.wms.studio.api.dto.user.UserDto;
import com.wms.studio.api.dto.user.UserInfoDto;
import com.wms.studio.api.dto.user.UserLoginDto;
import com.wms.studio.api.service.MailService;
import com.wms.studio.api.service.UserService;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.covert.EntityConvertInterface;
import com.wms.studio.entity.LoginIp;
import com.wms.studio.entity.User;
import com.wms.studio.entity.Wallpaper;
import com.wms.studio.exception.VerificationException;
import com.wms.studio.repository.LoginIpRepository;
import com.wms.studio.repository.UserRepository;
import com.wms.studio.repository.WallpaperRepository;
import com.wms.studio.security.utils.Cryptos;
import com.wms.studio.security.utils.Digests;
import com.wms.studio.service.user.UserMailHandler;
import com.wms.studio.service.user.UserMailHandlerFactoryBean;
import com.wms.studio.utils.Encodes;
import com.wms.studio.utils.SortUtils;

/**
 * 发送邮件的信息组装和验证邮件的逻辑可以通过抽离出来，但是因为时间关系，这儿暂时不进行修改，将验证逻辑使用处理类进行处理
 * 
 * @author WMS
 * 
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	private static final Logger log = Logger.getLogger(UserServiceImpl.class);

	@Resource
	private UserRepository userRepository;

	@Resource
	private LoginIpRepository loginIpRepository;

	@Resource
	private WallpaperRepository wallpaperRepository;

	@Resource
	private MailService mailService;

	@Resource
	private UserMailHandlerFactoryBean handler;

	@Value("#{props['system.mailSalt']}")
	private String mailSalt;

	@Resource
	@Qualifier("userInfoCovert")
	private EntityConvertInterface<User, UserInfoDto> userInfoCovert;

	public void setWallpaperRepository(WallpaperRepository wallpaperRepository) {
		this.wallpaperRepository = wallpaperRepository;
	}

	public void setLoginIpRepository(LoginIpRepository loginIpRepository) {
		this.loginIpRepository = loginIpRepository;
	}

	public void setMailSalt(String mailSalt) {
		this.mailSalt = mailSalt;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setHandler(UserMailHandlerFactoryBean handler) {
		this.handler = handler;
	}

	public void setUserInfoCovert(
			EntityConvertInterface<User, UserInfoDto> userInfoCovert) {
		this.userInfoCovert = userInfoCovert;
	}

	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 */
	public void entryptPassword(User user) {

		byte[] salt = Digests.generateSalt(UserConstant.SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt,
				UserConstant.HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}

	public String entryptPassword(String password, String saltString) {
		byte[] salt = Encodes.decodeHex(saltString);
		byte[] hashPassword = Digests.sha1(password.getBytes(), salt,
				UserConstant.HASH_INTERATIONS);
		return Encodes.encodeHex(hashPassword);
	}

	@Transactional
	@HandlerPoint(handlerName = "registerUserHandler")
	public CommonResponseDto registerUser(UserDto userDto) {

		CommonResponseDto responseDto = new CommonResponseDto(
				UserConstant.ERROR);
		try {

			if (userDto == null) {
				log.info("[注册用户]-[校验参数]-[UserDto不允许为空]");
				throw new VerificationException("参数校验错误");
			}

			if (!StringUtils.isLengthValid(userDto.getId(),
					UserConstant.USER_ID_MIN_LENGTH,
					UserConstant.USER_ID_MAX_LENGTH)) {
				log.info("[注册用户]-[校验用户账号参数]-[用户账号参数不合法]");
				throw new VerificationException("用户账号参数不合法");
			}

			if (!StringUtils.isLengthValid(userDto.getName(),
					UserConstant.USER_NAME_MIN_LENGTH,
					UserConstant.USER_NAME_MAX_LENGTH)) {
				log.info("[注册用户]-[校验用户姓名参数]-[用户姓名参数不合法]");
				throw new VerificationException("用户姓名参数不合法");
			}

			if (!StringUtils.isLengthValid(userDto.getPassword(),
					UserConstant.USER_PASSWORD_MIN_LENGTH,
					UserConstant.USER_PASSWORD_MAX_LENGTH)) {
				log.info("[注册用户]-[校验密码参数]-[密码参数不合法]");
				throw new VerificationException("密码参数不合法");
			}

			/*
			 * if (!StringUtils.checkEmail(userDto.getEmail())) {
			 * log.info("[注册用户]-[校验注册邮箱参数]-[注册邮箱参数不合法]"); throw new
			 * VerificationException("注册邮箱参数不合法"); }
			 */
			CommonResponseDto emailValid = isValidEmail(userDto.getEmail());

			if (UserConstant.SUCCESS != emailValid.getResult()) {
				return emailValid;
			}

			if (userRepository.exists(userDto.getId())) {
				log.info("[注册用户]-[校验用户账号]-[用户账号已经被注册]");
				throw new VerificationException("用户账号已经被注册");
			}

			User user = new User();
			user.setId(userDto.getId());
			user.setName(userDto.getName());
			user.setPassword(userDto.getPassword());
			user.setEmail(userDto.getEmail());
			user.setRole(userDto.getRole());
			entryptPassword(user);

			responseDto.setResult(UserConstant.SUCCESS);
			userRepository.save(user);
			LoginIp loginIp = new LoginIp(user, userDto.getRegisterAddress(),
					LoginType.注册);
			loginIpRepository.save(loginIp);

		} catch (VerificationException e) {
			responseDto.setErrorMessage(e.getMessage());
		}

		return responseDto;
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public CommonResponseDto isValidEmail(String email) {

		CommonResponseDto responseDto = new CommonResponseDto(
				UserConstant.ERROR);

		try {

			if (!StringUtils.checkEmail(email)) {
				log.info("[校验邮箱]-[校验邮箱参数]-[邮箱参数不合法]");
				throw new VerificationException("邮箱参数不合法");
			}

			boolean isValid = userRepository.existsByEmail(email).intValue() == 1;

			if (isValid) {
				log.info("[校验邮箱]-[邮箱已经被注册]");
				responseDto.setResult(UserConstant.EMAIL_INVALID);
				throw new VerificationException("邮箱已经被注册");
			}

			responseDto.setResult(UserConstant.SUCCESS);

		} catch (VerificationException e) {
			responseDto.setErrorMessage(e.getMessage());
		}

		return responseDto;
	}

	@HandlerPoint(handlerName = "userLoginHandler")
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public UserLoginDto login(String id) {

		UserLoginDto userLoginDto = new UserLoginDto();

		userLoginDto.setResult(UserConstant.ERROR);

		try {

			if (!StringUtils.isLengthValid(id, UserConstant.USER_ID_MIN_LENGTH,
					UserConstant.USER_ID_MAX_LENGTH)) {
				log.info("[用户登录]-[校验用户账号参数]-[用户账号参数不合法]");
				throw new VerificationException("用户账号参数不合法");
			}

			List<Object[]> objs = userRepository.findUserByIdForLogin(id);

			if (objs == null || objs.size() == 0) {
				log.info("[用户登录]-[校验用户账号参数]-[用户账号不存在]");
				throw new VerificationException("用户账号不存在");
			}
			userLoginDto.setId(id);
			userLoginDto.setName((String) objs.get(0)[0]);
			userLoginDto.setPassword((String) objs.get(0)[1]);
			userLoginDto.setDisable((Boolean) objs.get(0)[2]);
			userLoginDto.setSalt((String) objs.get(0)[3]);
			userLoginDto.setvStatus((Boolean) objs.get(0)[4]);
			userLoginDto.setRole((UserRole) objs.get(0)[5]);
			userLoginDto.setDisableReason((UserDisableReason) objs.get(0)[6]);
			userLoginDto.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			userLoginDto.setErrorMessage(e.getMessage());
		}

		return userLoginDto;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public CommonResponseDto updateUserInfo(UpdateUserDto updateUserDto) {

		CommonResponseDto responseDto = new CommonResponseDto(
				UserConstant.ERROR);

		try {

			if (updateUserDto == null) {
				log.info("[更新用户信息]-[校验参数]-[参数不合法]");
				throw new VerificationException("参数不合法");
			}

			if (!StringUtils.isLengthValid(updateUserDto.getId(),
					UserConstant.USER_ID_MIN_LENGTH,
					UserConstant.USER_ID_MAX_LENGTH)) {
				log.info("[更新用户信息]-[校验用户账号参数]-[用户账号参数不合法]");
				throw new VerificationException("用户账号参数不合法");
			}

			User user = userRepository.findOne(updateUserDto.getId());

			if (user == null) {
				log.info("[更新用户信息]-[校验用户账号参数]-[用户账号不存在]");
				throw new VerificationException("用户账号不存在");
			}

			if (!StringUtils.isLengthValid(updateUserDto.getName(),
					UserConstant.USER_NAME_MIN_LENGTH,
					UserConstant.USER_NAME_MAX_LENGTH)) {
				log.info("[更新用户信息]-[校验用户姓名参数]-[用户姓名参数不合法]");
				throw new VerificationException("用户姓名参数不合法");
			}

			if (!StringUtils.isLengthValid(updateUserDto.getPassword(),
					UserConstant.USER_PASSWORD_MIN_LENGTH,
					UserConstant.USER_PASSWORD_MAX_LENGTH)) {
				log.info("[更新用户信息]-[校验密码参数]-[密码参数不合法]");
				throw new VerificationException("密码参数不合法");
			}

			String entryptPassword = entryptPassword(
					updateUserDto.getPassword(), user.getSalt());

			if (!StringUtils.equals(user.getPassword(), entryptPassword)) {
				log.info("[更新用户信息]-[校验密码参数]-[密码错误]");
				throw new VerificationException("密码错误");
			}

			if (StringUtils.equals(user.getName(), updateUserDto.getName())) {
				log.info("[更新用户信息]-[校验用户姓名参数]-[用户姓名没有进行修改]");
				throw new VerificationException("用户姓名没有进行修改");
			}
			// 事务会自动提交
			user.setName(updateUserDto.getName());
			responseDto.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			responseDto.setErrorMessage(e.getMessage());
		}

		return responseDto;
	}

	@Transactional()
	public CommonResponseDto checkEmailForUser(String userId, String value) {

		CommonResponseDto responseDto = new CommonResponseDto(
				UserConstant.ERROR);

		try {

			if (!StringUtils.isLengthValid(userId,
					UserConstant.USER_ID_MIN_LENGTH,
					UserConstant.USER_ID_MAX_LENGTH)) {
				log.info("[邮箱验证]-[校验用户账号参数]-[用户账号参数不合法]");
				throw new VerificationException("账号参数不合法");
			}

			if (StringUtils.isBlank(value)) {
				log.info("[邮箱验证]-[校验验证参数]-[验证参数不合法]");
				throw new VerificationException("验证参数不合法");
			}

			String emailJsonValues = Cryptos.aesDecrypt(
					Encodes.decodeHex(value), Encodes.decodeHex(mailSalt));

			CheckEmailDto dto = JSON
					.parse(emailJsonValues, CheckEmailDto.class);

			if (dto == null) {
				throw new VerificationException("验证参数不合法");
			}

			if (!userId.equals(dto.getUserId())) {
				throw new VerificationException("验证参数不合法");
			}

			DateTime dateTime = new DateTime(dto.getLastTime());

			if (dateTime.isBeforeNow()) {
				throw new VerificationException("链接已经失效");
			}

			User user = userRepository.findOne(userId);

			if (user == null) {
				throw new VerificationException("用户不存在");
			}

			handler.getHandler(dto.getType()).handlerMailResult(user, dto);

			responseDto.setResult(UserConstant.SUCCESS);
			responseDto.setErrorMessage(dto.getType());
		} catch (VerificationException e) {
			responseDto.setErrorMessage(e.getMessage());
		} catch (Exception e) {
			log.info("[邮箱验证]-[校验验证参数]-[验证参数转换异常]", e);
			responseDto.setErrorMessage("验证参数不合法");
		}

		return responseDto;
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public CommonResponseDto changeEmail(UpdateUserDto updateUserDto) {
		CommonResponseDto responseDto = new CommonResponseDto(
				UserConstant.ERROR);

		try {

			if (updateUserDto == null) {
				log.info("[更新用户信息]-[校验参数]-[参数不合法]");
				throw new VerificationException("参数不合法");
			}

			if (!StringUtils.isLengthValid(updateUserDto.getId(),
					UserConstant.USER_ID_MIN_LENGTH,
					UserConstant.USER_ID_MAX_LENGTH)) {
				log.info("[更新用户信息]-[校验用户账号参数]-[用户账号参数不合法]");
				throw new VerificationException("用户账号参数不合法");
			}

			User user = userRepository.findOne(updateUserDto.getId());

			if (user == null) {
				log.info("[更新用户信息]-[校验用户账号参数]-[用户账号不存在]");
				throw new VerificationException("用户账号不存在");
			}

			if (!StringUtils.isLengthValid(updateUserDto.getPassword(),
					UserConstant.USER_PASSWORD_MIN_LENGTH,
					UserConstant.USER_PASSWORD_MAX_LENGTH)) {
				log.info("[更新用户信息]-[校验密码参数]-[密码参数不合法]");
				throw new VerificationException("密码参数不合法");
			}

			String entryptPassword = entryptPassword(
					updateUserDto.getPassword(), user.getSalt());

			if (!StringUtils.equals(user.getPassword(), entryptPassword)) {
				log.info("[更新用户信息]-[校验密码参数]-[密码错误]");
				throw new VerificationException("密码错误");
			}

			if (StringUtils.equals(user.getEmail(), updateUserDto.getEmail())) {
				log.info("[更新用户信息]-[校验邮箱参数]-[邮箱参数没有进行修改]");
				throw new VerificationException("邮箱参数没有进行修改");
			}

			if (userRepository.findByEmail(updateUserDto.getEmail()) != null) {
				throw new VerificationException("该邮箱已经被注册");
			}

			UserMailHandler mailHandler = handler
					.getHandler(EmailConstant.EMAIL_USER_CHANGE_EMAIL);

			MailMessage mailMessage = mailHandler.handlerMailMessage(
					updateUserDto.getEmail(), user);

			CommonResponseDto mailResponseDto = mailService
					.sendValidEmail(mailMessage);

			if (UserConstant.SUCCESS == mailResponseDto.getResult()) {
				responseDto.setResult(UserConstant.SUCCESS);
			} else {
				return mailResponseDto;
			}
		} catch (VerificationException e) {
			responseDto.setErrorMessage(e.getMessage());
		}

		return responseDto;
	}

	public CommonResponseDto changeUserPassword(UpdateUserDto updateUserDto) {
		CommonResponseDto responseDto = new CommonResponseDto(
				UserConstant.ERROR);

		try {

			if (updateUserDto == null) {
				log.info("[更新用户信息]-[校验参数]-[参数不合法]");
				throw new VerificationException("参数不合法");
			}

			if (!StringUtils.isLengthValid(updateUserDto.getId(),
					UserConstant.USER_ID_MIN_LENGTH,
					UserConstant.USER_ID_MAX_LENGTH)) {
				log.info("[更新用户信息]-[校验用户账号参数]-[用户账号参数不合法]");
				throw new VerificationException("用户账号参数不合法");
			}

			User user = userRepository.findOne(updateUserDto.getId());

			if (user == null) {
				log.info("[更新用户信息]-[校验用户账号参数]-[用户账号不存在]");
				throw new VerificationException("用户账号不存在");
			}

			if (!StringUtils.isLengthValid(updateUserDto.getModifyPassword(),
					UserConstant.USER_PASSWORD_MIN_LENGTH,
					UserConstant.USER_PASSWORD_MAX_LENGTH)) {
				log.info("[更新用户信息]-[校验密码参数]-[密码参数不合法]");
				throw new VerificationException("密码参数不合法");
			}

			if (!StringUtils.isLengthValid(updateUserDto.getPassword(),
					UserConstant.USER_PASSWORD_MIN_LENGTH,
					UserConstant.USER_PASSWORD_MAX_LENGTH)) {
				log.info("[更新用户信息]-[校验密码参数]-[密码参数不合法]");
				throw new VerificationException("密码参数不合法");
			}

			String entryptPassword = entryptPassword(
					updateUserDto.getPassword(), user.getSalt());

			if (!StringUtils.equals(user.getPassword(), entryptPassword)) {
				log.info("[更新用户信息]-[校验密码参数]-[密码错误]");
				throw new VerificationException("密码错误");
			}

			if (StringUtils.equals(user.getPassword(),
					updateUserDto.getModifyPassword())) {
				log.info("[更新用户信息]-[校验用户姓名参数]-[用户姓名没有进行修改]");
				throw new VerificationException("密码不允许和之前一致");
			}
			// 事务会自动提交
			user.setPassword(updateUserDto.getModifyPassword());
			entryptPassword(user);
			responseDto.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			responseDto.setErrorMessage(e.getMessage());
		}

		return responseDto;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public CommonResponseDto findPassword(String email) {

		CommonResponseDto responseDto = isValidEmail(email);

		try {

			if (UserConstant.EMAIL_INVALID != responseDto.getResult()) {
				if (UserConstant.SUCCESS == responseDto.getResult()) {
					responseDto.setErrorMessage("邮箱未注册");
				}
				throw new VerificationException(responseDto.getErrorMessage());
			}

			User user = userRepository.findByEmail(email);

			if (user == null) {
				throw new VerificationException("用户不存在");
			}

			if (user.isDisable() || !user.isvStatus()) {
				throw new VerificationException("用户被禁用或者邮箱未验证");
			}

			UserMailHandler mailHandler = handler
					.getHandler(EmailConstant.EMAIL_USER_FIND_PASSWORD);

			MailMessage mailMessage = mailHandler.handlerMailMessage(email,
					user);

			CommonResponseDto mailResponseDto = mailService
					.sendValidEmail(mailMessage);

			if (UserConstant.SUCCESS == mailResponseDto.getResult()) {
				responseDto.setResult(UserConstant.SUCCESS);
			} else {
				return mailResponseDto;
			}

		} catch (VerificationException e) {
			responseDto.setResult(UserConstant.ERROR);
			responseDto.setErrorMessage(e.getMessage());
		}

		return responseDto;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public WallpaperDto getUserTheme(String userId) {

		WallpaperDto responseDto = new WallpaperDto(UserConstant.ERROR);

		try {

			if (!StringUtils.isLengthValid(userId,
					UserConstant.USER_ID_MIN_LENGTH,
					UserConstant.USER_ID_MAX_LENGTH)) {
				log.info("[用户账号参数不合法]");
				throw new VerificationException("用户账号参数不合法");
			}
			Wallpaper wallpaper = null;
			Integer wallpaperId = userRepository.getUserTheme(userId);

			if (wallpaperId == null || wallpaperId < 1) {
				List<Wallpaper> ws = wallpaperRepository.findByIsDefaultTrue();
				if (ws == null || ws.isEmpty()) {
					log.fatal("错误，请管理员配置默认壁纸");
					throw new VerificationException("系统错误");
				}
				wallpaper = ws.get(0);
			} else {
				wallpaper = wallpaperRepository.findOne(wallpaperId);
			}

			responseDto.setId(wallpaper.getId());
			responseDto.setPath(wallpaper.getPath());
			responseDto.setResult(UserConstant.SUCCESS);

		} catch (VerificationException e) {
			responseDto.setErrorMessage(e.getMessage());
		}
		return responseDto;
	}

	@Override
	@Transactional
	public void setUserTheme(String userId, Integer wallpaperId) {
		try {
			if (!StringUtils.isLengthValid(userId,
					UserConstant.USER_ID_MIN_LENGTH,
					UserConstant.USER_ID_MAX_LENGTH)) {
				log.info("[用户账号参数不合法]");
				throw new VerificationException();
			}
			if (wallpaperId == null || wallpaperId < 1) {
				throw new VerificationException();
			}
			this.userRepository.updateUserWallpaper(userId, wallpaperId);
		} catch (VerificationException e) {
		} catch (Exception e) {
		}
	}

	@Override
	public UserInfoDto getUserInfo(String userId) {

		try {
			if (!StringUtils.isLengthValid(userId,
					UserConstant.USER_ID_MIN_LENGTH,
					UserConstant.USER_ID_MAX_LENGTH)) {
				log.info("[用户账号参数不合法]");
				throw new VerificationException();
			}

			User user = this.userRepository.findOne(userId);

			return userInfoCovert.covertToDto(user);
		} catch (VerificationException e) {
		}
		return null;
	}

	@Override
	public PageDto<UserInfoDto> listUserInfos(PageSize pageSize, SortDto sortDto) {

		if (pageSize == null) {
			pageSize = new PageSize();
		}

		return userInfoCovert.covertToDto(userRepository
				.findAll(new PageRequest(pageSize.getPage() - 1, pageSize
						.getLimit(), SortUtils.covertSortDto(sortDto))));
	}

	@Override
	public CommonResponseDto findPassword(String userId, String password) {

		CommonResponseDto responseDto = new CommonResponseDto(
				UserConstant.ERROR);

		try {
			if (!StringUtils.isLengthValid(userId,
					UserConstant.USER_ID_MIN_LENGTH,
					UserConstant.USER_ID_MAX_LENGTH)) {
				throw new VerificationException("用户账号参数不合法");
			}

			if (!StringUtils.isLengthValid(password,
					UserConstant.USER_PASSWORD_MIN_LENGTH,
					UserConstant.USER_PASSWORD_MAX_LENGTH)) {
				throw new VerificationException("密码参数不合法");
			}
			User user = this.userRepository.findOne(userId);

			if (user == null) {
				throw new VerificationException("用户账号不存在");
			}
			user.setPassword(password);

			entryptPassword(user);

			responseDto.setResult(UserConstant.SUCCESS);
		} catch (VerificationException e) {
			responseDto.setErrorMessage(e.getMessage());
		}
		return responseDto;
	}

}
