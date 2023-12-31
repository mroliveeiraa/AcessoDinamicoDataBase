import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Service;

/*
criar um serviço chamado DataSourceRoutingService que gerenciará qual fonte de dados está ativa com base nas decisões do aspecto.

Este serviço estende AbstractRoutingDataSource e determina a fonte de dados com base em um ThreadLocal, 
que é configurado pelo aspecto. Ele fornece métodos setReadDataSource() e setWriteDataSource() para definir a fonte de dados apropriada.
  */

@Service
public class DataSourceRoutingService extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> dataSourceHolder = new ThreadLocal<>();

    public void setReadDataSource() {
        dataSourceHolder.set("read");
        determineCurrentLookupKey();
    }

    public void setWriteDataSource() {
        dataSourceHolder.set("write");
        determineCurrentLookupKey();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceHolder.get();
    }
}
