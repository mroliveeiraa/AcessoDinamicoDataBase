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

    // Configuração da fonte de dados que será usada para leitura
    @Bean
    @Qualifier("readDataSource")
    public DataSource readDataSource(
            @Value("${spring.datasource.read.url}") String url,
            @Value("${spring.datasource.read.username}") String username,
            @Value("${spring.datasource.read.password}") String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver"); // Driver JDBC específico para o PostgreSQL
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    // Configuração da fonte de dados que será usada para gravação
    @Bean
    @Qualifier("writeDataSource")
    public DataSource writeDataSource(
            @Value("${spring.datasource.write.url}") String url,
            @Value("${spring.datasource.write.username}") String username,
            @Value("${spring.datasource.write.password}") String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver"); // Driver JDBC específico para o PostgreSQL
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    // Configuração do EntityManagerFactory para a fonte de dados de leitura
    @Bean
    @Qualifier("readEntityManager")
    public LocalContainerEntityManagerFactoryBean readEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("readDataSource") DataSource dataSource,
            JpaProperties jpaProperties) {
        Map<String, String> readJpaProperties = new HashMap<>(jpaProperties.getProperties());

    String[] entityPackages = {
        "seu.pacote.de.entidades1",
        "seu.pacote.de.entidades2"
    };
       
        return builder
                .dataSource(dataSource)
                .packages(entityPackages) // Substitua pelo pacote de suas entidades
                .properties(readJpaProperties)
                .persistenceUnit("readEntityManager")
                .build();
    }

    // Configuração do EntityManagerFactory para a fonte de dados de gravação
    @Bean
    @Qualifier("writeEntityManager")
    public LocalContainerEntityManagerFactoryBean writeEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("writeDataSource") DataSource dataSource,
            JpaProperties jpaProperties) {
        Map<String, String> writeJpaProperties = new HashMap<>(jpaProperties.getProperties());

        return builder
                .dataSource(dataSource)
                .packages("seu.pacote.de.entidades") // Substitua pelo pacote de suas entidades
                .properties(writeJpaProperties)
                .persistenceUnit("writeEntityManager")
                .build();
    }

    // Configuração do gerenciador de transações para a fonte de dados de leitura
    @Bean
    public PlatformTransactionManager readTransactionManager(
            @Qualifier("readEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    // Configuração do gerenciador de transações para a fonte de dados de gravação
    @Bean
    public PlatformTransactionManager writeTransactionManager(
            @Qualifier("writeEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
