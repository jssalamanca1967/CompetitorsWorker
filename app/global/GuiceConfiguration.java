package global;

import com.google.inject.AbstractModule;


/**
 * Created by John on 07/10/2016.
 */
public class GuiceConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        bind(OnStartupService.class).asEagerSingleton();
    }

}
