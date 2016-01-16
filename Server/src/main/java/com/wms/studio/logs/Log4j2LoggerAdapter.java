package com.wms.studio.logs;

import com.alibaba.dubbo.common.logger.Level;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerAdapter;
import com.alibaba.dubbo.common.logger.support.FailsafeLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.util.ReflectionUtil;

import java.io.File;

/**
 * Created by hzwumsh on 2016/1/16.
 */
public class Log4j2LoggerAdapter implements LoggerAdapter {

    private static final String FQCN = FailsafeLogger.class.getName();
    private static volatile LoggerContextFactory factory = LogManager.getFactory();


    public LoggerContext getContext(ClassLoader loader, boolean currentContext) {
        return factory.getContext(FQCN, loader, null, currentContext);
    }

    public LoggerContext getContext(boolean currentContext) {
        return factory.getContext(FQCN, null, null, currentContext, null, null);
    }

    private Class<?> callerClass(Class<?> clazz) {
        if (clazz != null) {
            return clazz;
        } else {
            Class candidate = ReflectionUtil.getCallerClass(3);
            if (candidate == null) {
                throw new UnsupportedOperationException("No class provided, and an appropriate one cannot be found.");
            } else {
                return candidate;
            }
        }
    }

    @Override
    public Logger getLogger(Class<?> aClass) {
        Class cls = callerClass(aClass);
        org.apache.logging.log4j.Logger logger = getContext(cls.getClassLoader(), false).getLogger(cls.getName());
        return new Log4j2Logger(logger);
    }

    @Override
    public Logger getLogger(String name) {

        if (name == null) {
            return getLogger(ReflectionUtil.getCallerClass(2));
        }
        org.apache.logging.log4j.Logger logger = getContext(false).getLogger(name);
        return new Log4j2Logger(logger);
    }

    @Override
    public Level getLevel() {
        return null;
    }

    @Override
    public void setLevel(Level level) {
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public void setFile(File file) {

    }
}
