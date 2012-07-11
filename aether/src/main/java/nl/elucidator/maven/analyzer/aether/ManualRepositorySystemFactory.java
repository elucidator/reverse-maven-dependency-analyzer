package nl.elucidator.maven.analyzer.aether;

import org.apache.maven.repository.internal.DefaultServiceLocator;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.connector.file.FileRepositoryConnectorFactory;
import org.sonatype.aether.connector.wagon.WagonProvider;
import org.sonatype.aether.connector.wagon.WagonRepositoryConnectorFactory;
import org.sonatype.aether.spi.connector.RepositoryConnectorFactory;

/**
 * A factory for repository system instances that employs Aether's built-in service locator infrastructure to wire up
 * the system's components.
 */
public class ManualRepositorySystemFactory
{

    public static RepositorySystem newRepositorySystem()
    {
        /*
         * Aether's components implement org.eclipse.aether.spi.locator.Service to ease manual wiring and using the
         * prepopulated DefaultServiceLocator, we only need to register the repository connector factories.
         */
        DefaultServiceLocator locator = new DefaultServiceLocator();
        locator.addService( RepositoryConnectorFactory.class, FileRepositoryConnectorFactory.class );
        locator.addService( RepositoryConnectorFactory.class, WagonRepositoryConnectorFactory.class );
        locator.setServices( WagonProvider.class, new ManualWagonProvider() );

        return locator.getService( RepositorySystem.class );
    }

}
