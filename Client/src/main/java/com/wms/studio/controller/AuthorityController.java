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

package com.wms.studio.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wms.studio.api.constant.UserRole;
import com.wms.studio.api.dto.ComboboxStoreDto;
import com.wms.studio.exception.VerificationException;

/**
 * @author WMS
 * 
 */
@Controller
public class AuthorityController {

	@RequestMapping(value = "/auth/getRoles", method = RequestMethod.POST)
	public void getRoleList(Model model) {
		List<ComboboxStoreDto> dtos = new ArrayList<>();
		dtos.add(new ComboboxStoreDto(UserRole.用户.name()));
		try {
			Subject subject = SecurityUtils.getSubject();
			if (subject == null) {
				throw new VerificationException();
			}
			if (subject.hasRole(UserRole.开发者.getRole())) {
				dtos.add(new ComboboxStoreDto(UserRole.开发者.name()));
			}
			if (subject.hasRole(UserRole.管理员.getRole())) {
				dtos.add(new ComboboxStoreDto(UserRole.管理员.name()));
			}
		} catch (VerificationException e) {

		}
		model.addAttribute("data", dtos);
	}

}
