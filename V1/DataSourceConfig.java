import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/*
No seu arquivo de configuração, configure o DataSource com as fontes de dados de leitura e gravação e defina o DataSourceRoutingService como o targetDataSource. 
O Spring AOP e o aspecto configurados anteriormente direcionarão as chamadas para a fonte de dados correta.
*/


@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Bean
    public DataSourceRoutingService dataSourceRoutingService() {
        return new DataSourceRoutingService();
    }

    @Bean
    public DataSource dataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("read", readDataSource());
        targetDataSources.put("write", writeDataSource());

        DataSourceRoutingDataSource routingDataSource = new DataSourceRoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(writeDataSource());

        return routingDataSource;
    }

    @Bean
    public DataSource readDataSource() {
        // Configurar sua fonte de dados de leitura aqui
    }

    @Bean
    public DataSource writeDataSource() {
        // Configurar sua fonte de dados de gravação aqui
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
