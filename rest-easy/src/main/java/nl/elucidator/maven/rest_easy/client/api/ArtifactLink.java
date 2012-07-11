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

package nl.elucidator.maven.rest_easy.client.api;

import javax.xml.bind.annotation.XmlRootElement;

/**
 classifier (string)	0/1	 The classifier of the artifact.
 extension (string)	0/1	 The extension of the artifact.
 */
@XmlRootElement
public class ArtifactLink implements Visitable {
    private static final String INDENT = "\n\t\t\t\t";
    // (string)	0/1	 The classifier of the artifact.
    String classifier;
    // (string)	0/1	 The extension of the artifact.
    String extension;

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(final String classifier) {
        this.classifier = classifier;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(final String extension) {
        this.extension = extension;
    }

    @Override
    public void accept(final ResponseVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return INDENT + "ArtifactLink{" +
                INDENT + " classifier='" + classifier + '\'' +
                INDENT + " extension='" + extension + '\'' +
                INDENT + '}' + "\n\t\t\t";
    }
}
