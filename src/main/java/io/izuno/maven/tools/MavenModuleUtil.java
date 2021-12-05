package io.izuno.maven.tools;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavenModuleUtil {

    private static final Logger logger = LoggerFactory.getLogger(MavenModuleUtil.class);

    private MavenModuleUtil() {
    }

    public static List<ModuleInfo> searchModulesInfo() {
        String filename;
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> rs = cl.getResources("META-INF/maven");

            if (!rs.hasMoreElements()) {
                return Collections.emptyList();
            }

            URL url = rs.nextElement();
            if (!StringUtils.containsAny(url.getPath(), "!")) {
                return Collections.emptyList();
            }

            String osName = System.getProperty("os.name");
            if (osName.startsWith("Windows")) {
                filename = getJarFileNameForWindows(url);
            } else if (osName.startsWith("Linux") || osName.startsWith("Mac")) {
                filename = getJarFileNameForLinux(url);
            } else {
                return Collections.emptyList();
            }

            return searchModulesInfo(Paths.get(filename).toFile());
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public static List<ModuleInfo> searchModulesInfo(File warfile) {

        List<ModuleInfo> result = new ArrayList<>();
        try (JarFile jar = new JarFile(warfile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith("META-INF/maven/") && entry.getName().endsWith("pom.properties")) {
                    Properties p = new Properties();
                    p.load(jar.getInputStream(entry));
                    ModuleInfo info = new ModuleInfo();
                    info.setArtifactId(p.getProperty("artifactId"));
                    info.setGroupId(p.getProperty("groupId"));
                    info.setVersion(p.getProperty("version"));
                    result.add(info);
                }
            }
        } catch (IOException e) {
            logger.error("jarファイルが取得できませんでした。", e);
            return result;
        }
        return result;
    }

    public static List<ModuleInfo> searchModulesInfo(String groupPrefix) {
        List<ModuleInfo> result = new ArrayList<>();
        List<ModuleInfo> tmp = searchModulesInfo();
        for (ModuleInfo info : tmp) {
            if (info.getGroupId().startsWith(groupPrefix)) {
                result.add(info);
            }
        }

        return result;
    }

    private static String getJarFileNameForWindows(URL maven) {
        String filepath = maven.getPath();
        logger.debug("java file path expression:{}", filepath);
        String jarFileName = filepath.substring(6, filepath.indexOf('!')).replaceAll("%20", " ");
        logger.debug("jar file path:{}", jarFileName);
        return jarFileName;
    }

    private static String getJarFileNameForLinux(URL maven) {
        String filepath = maven.getPath();
        logger.debug("java file path expression:{}", filepath);
        String jarFileName = filepath.substring(5, filepath.indexOf('!')).replaceAll("%20", " ");
        logger.debug("jar file path:{}", jarFileName);
        return jarFileName;
    }

}
