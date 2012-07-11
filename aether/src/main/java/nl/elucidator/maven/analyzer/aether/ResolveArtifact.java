package nl.elucidator.maven.analyzer.aether;

import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.collection.CollectResult;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.artifact.JavaScopes;

//import org.sonatype.aether.artifact.DefaultArtifact;

/**
 * Resolves a single artifact.
 */
public class ResolveArtifact
{

    public static void main( String[] args )
            throws Exception
    {
        System.out.println( "------------------------------------------------------------" );
        System.out.println( ResolveArtifact.class.getSimpleName() );

        RepositorySystem system = Booter.newRepositorySystem();

        RepositorySystemSession session = Booter.newRepositorySystemSession( system );

        RemoteRepository repo = Booter.newCentralRepository();


//        DependencyFilter classpathFlter = DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE);

        //DefaultArtifact artifact = new DefaultArtifact("org.apache.maven.shared:maven-dependency-analyzer:1.2");
        DefaultArtifact artifact = new DefaultArtifact("nl.pieni.maven.dependency-analyzer:maven-dependency-analyzer:0.8");
        //DefaultArtifact artifact = new DefaultArtifact("org.apache.maven.plugins:maven-compiler-plugin:2.3");

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot( new Dependency( artifact, JavaScopes.COMPILE ) );
        collectRequest.addRepository( repo );

        CollectResult collectResult = system.collectDependencies( session, collectRequest );

        System.out.println("Graph");
        collectResult.getRoot().accept( new ConsoleDependencyGraphDumper() );
        System.out.println("Transitive");
        collectResult.getRoot().accept(new TransitiveDependencyGraphDumper());
        System.out.println("Flat");
        collectResult.getRoot().accept(new FlatDependencyGraphDumper());
    }

}


