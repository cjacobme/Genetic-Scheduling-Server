package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.entity.configuration.ConfigurationHolderTestConfiguration;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ConfigurationDumper.class)
@Import(ConfigurationHolderTestConfiguration.class)
class ConfigurationDumperTest {

    @Test
    void metadata() {
        Component component = ConfigurationDumper.class.getAnnotation(Component.class);
        Class<?>[] interfaces = ConfigurationDumper.class.getInterfaces();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(component).as("@Component").isNotNull();
        softy.assertThat(interfaces).as("interfaces").contains(InitializingBean.class);
        softy.assertAll();
    }
}
