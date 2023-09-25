import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/*
Neste exemplo, o aspecto DataSourceRoutingAspect intercepta chamadas de método anotadas com @Transactional. 
Ele verifica se a anotação possui readOnly definido como true e, com base nisso, 
chama dataSourceRoutingService para definir a fonte de dados apropriada (leitura ou gravação).
 */


@Aspect
@Component
public class DataSourceRoutingAspect {

    @Autowired
    private DataSourceRoutingService dataSourceRoutingService;

    @Before("@annotation(transactional)")
    public void setDataSourceBasedOnAnnotation(Transactional transactional) {
        if (transactional.readOnly()) {
            dataSourceRoutingService.setReadDataSource();
        } else {
            dataSourceRoutingService.setWriteDataSource();
        }
    }
}
