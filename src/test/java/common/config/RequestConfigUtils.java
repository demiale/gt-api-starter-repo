package common.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


class RequestConfigUtils {

    static final Properties props = new Properties();

    static {
        loadRequestProps();
    }

     static void loadRequestProps() {

        try (
                InputStream inputStream = RequestConfigUtils.class
                        .getClassLoader().getResourceAsStream("requestConfig.properties");
        ) {

            props.load(inputStream);

        } catch (IOException e) {
            System.err.println("File does not exist");
            e.printStackTrace();
        }

    }

}
