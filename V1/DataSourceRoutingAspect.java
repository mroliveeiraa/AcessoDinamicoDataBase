import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
