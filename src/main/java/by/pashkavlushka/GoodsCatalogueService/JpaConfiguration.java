/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.GoodsCatalogueService;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.transaction.TransactionManager;
import java.beans.PropertyVetoException;
import java.util.Properties;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.hibernate.HibernateTransactionManager;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JpaConfiguration {
    
    @Bean
    public Properties properties(){
        Properties properties = new Properties();
        properties.setProperty("user", "postgres");
        properties.setProperty("password", "100B032400");
        properties.setProperty("PGDBNAME", "online_shop");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.id.new_generator_mappings", "true");//нужно, чтобы jpa синхронизировался с последовательностью самой БД, иначе будут отрицательные значения в ID
        return properties;
    }
    
    @Bean
    public DataSource dataSource(Properties properties) throws PropertyVetoException{
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("org.postgresql.Driver");
        dataSource.setMaxPoolSize(50);
        dataSource.setDataSourceName("default");
        dataSource.setUser("postgres");//change to config server
        dataSource.setPassword("100B032400");//change to config server
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/online_shop");
        
        
        dataSource.setProperties(properties);
        
        return dataSource;
    }
    
    @Bean
    public PersistenceUnitManager persistenceUnitManager(DataSource dataSource){
        GoodsPersistenceUnitInfo info = new GoodsPersistenceUnitInfo(){
                    @Override
                    public DataSource getNonJtaDataSource() {
                        return dataSource;
                    }
                    
                };
        return new PersistenceUnitManager() {
            @Override
            public PersistenceUnitInfo obtainDefaultPersistenceUnitInfo() throws IllegalStateException {
                return info;
            }

            @Override
            public PersistenceUnitInfo obtainPersistenceUnitInfo(String persistenceUnitName) throws IllegalArgumentException, IllegalStateException {
                if(persistenceUnitName.equals("goods")){
                    return info;
                }
                throw new IllegalArgumentException("No such PersistenceUnitInfo object found");
            }
        };
    }
    
    @Bean
    @DependsOn({"dataSource", "persistenceUnitManager"})
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("dataSource") DataSource dataSource, PersistenceUnitManager manager, Properties properties){
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPackagesToScan("entities");
        bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        bean.setPersistenceProvider(new HibernatePersistenceProvider());
        bean.setPersistenceUnitManager(manager);
        bean.setJpaProperties(properties);
        return bean;
    }
    
    @Bean
    @DependsOn("entityManagerFactory")
    public PlatformTransactionManager transactionManager(DataSource dataSource, EntityManagerFactory factory){
        JpaTransactionManager manager = new JpaTransactionManager(factory);
        manager.setRollbackOnCommitFailure(true);
        manager.setDataSource(dataSource);
        manager.setPersistenceUnitName("goods");
        return manager;
    }
    
}
