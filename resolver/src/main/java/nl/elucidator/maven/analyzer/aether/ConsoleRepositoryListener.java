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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.aether.AbstractRepositoryListener;
import org.sonatype.aether.RepositoryEvent;

import java.io.PrintStream;

/**
 * A simplistic repository listener that logs events to the console.
 */
public class ConsoleRepositoryListener
        extends AbstractRepositoryListener {

    private PrintStream out;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleRepositoryListener.class);
    private static boolean isTracing = false;

    public ConsoleRepositoryListener() {
        isTracing = LOGGER.isTraceEnabled();
    }

    public void artifactDeployed(RepositoryEvent event) {
        if (isTracing) {
            LOGGER.trace("Deployed " + event.getArtifact() + " to " + event.getRepository());
        }
    }

    public void artifactDeploying(RepositoryEvent event) {
        LOGGER.trace("Deploying " + event.getArtifact() + " to " + event.getRepository());
    }

    public void artifactDescriptorInvalid(RepositoryEvent event) {
        LOGGER.trace("Invalid artifact descriptor for " + event.getArtifact() + ": "
                + event.getException().getMessage());
    }

    public void artifactDescriptorMissing(RepositoryEvent event) {
        LOGGER.trace("Missing artifact descriptor for " + event.getArtifact());
    }

    public void artifactInstalled(RepositoryEvent event) {
        LOGGER.trace("Installed " + event.getArtifact() + " to " + event.getFile());
    }

    public void artifactInstalling(RepositoryEvent event) {
        LOGGER.trace("Installing " + event.getArtifact() + " to " + event.getFile());
    }

    public void artifactResolved(RepositoryEvent event) {
        LOGGER.trace("Resolved artifact " + event.getArtifact() + " from " + event.getRepository());
    }

    public void artifactDownloading(RepositoryEvent event) {
        LOGGER.trace("Downloading artifact " + event.getArtifact() + " from " + event.getRepository());
    }

    public void artifactDownloaded(RepositoryEvent event) {
        LOGGER.debug("Downloaded artifact " + event.getArtifact() + " from " + event.getRepository());
    }

    public void artifactResolving(RepositoryEvent event) {
        LOGGER.trace("Resolving artifact " + event.getArtifact());
    }

    public void metadataDeployed(RepositoryEvent event) {
        LOGGER.trace("Deployed " + event.getMetadata() + " to " + event.getRepository());
    }

    public void metadataDeploying(RepositoryEvent event) {
        LOGGER.trace("Deploying " + event.getMetadata() + " to " + event.getRepository());
    }

    public void metadataInstalled(RepositoryEvent event) {
        LOGGER.trace("Installed " + event.getMetadata() + " to " + event.getFile());
    }

    public void metadataInstalling(RepositoryEvent event) {
        LOGGER.trace("Installing " + event.getMetadata() + " to " + event.getFile());
    }

    public void metadataInvalid(RepositoryEvent event) {
        LOGGER.trace("Invalid metadata " + event.getMetadata());
    }

    public void metadataResolved(RepositoryEvent event) {
        LOGGER.trace("Resolved metadata " + event.getMetadata() + " from " + event.getRepository());
    }

    public void metadataResolving(RepositoryEvent event) {
        LOGGER.trace("Resolving metadata " + event.getMetadata() + " from " + event.getRepository());
    }

}
