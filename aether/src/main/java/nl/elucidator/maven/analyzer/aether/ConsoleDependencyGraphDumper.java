/*
 * Copyright (C) 2012 Pieter van der Meer (pieter(at)elucidator.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.elucidator.maven.analyzer.aether;

import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.graph.DependencyVisitor;

import java.io.PrintStream;

/**
 * A dependency visitor that dumps the graph to the console.
 */
public class ConsoleDependencyGraphDumper
        implements DependencyVisitor
{

    private PrintStream out;

    private String currentIndent = "";

    public ConsoleDependencyGraphDumper()
    {
        this( null );
    }

    public ConsoleDependencyGraphDumper( PrintStream out )
    {
        this.out = ( out != null ) ? out : System.out;
    }

    public boolean visitEnter( DependencyNode node )
    {
        out.println( currentIndent + node );
        if ( currentIndent.length() <= 0 )
        {
            currentIndent = "+- ";
        }
        else
        {
            currentIndent = "|  " + currentIndent;
        }
        return true;
    }

    public boolean visitLeave( DependencyNode node )
    {
        currentIndent = currentIndent.substring( 3, currentIndent.length() );
        return true;
    }

}
