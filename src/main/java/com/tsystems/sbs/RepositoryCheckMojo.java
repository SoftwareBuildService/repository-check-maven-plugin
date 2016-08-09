package com.tsystems.sbs;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.SyncContext;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.ArtifactProperties;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.impl.ArtifactResolver;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.ArtifactRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.resolution.*;

import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;

import org.eclipse.aether.repository.LocalRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Goal which touches a timestamp file.
 *
 * @goal check-repository
 * @phase validate
 */
public class RepositoryCheckMojo extends AbstractMojo {
    /**
     * The entry point to Aether, i.e. the component doing all the work.
     *
     * @component
     */
    private RepositorySystem repoSystem;

    /**
     * The current repository/network configuration of Maven.
     *
     * @parameter default-value="${repositorySystemSession}"
     * @readonly
     */
    private RepositorySystemSession repoSession;
    /**
     * The project's remote repositories to use for the resolution of plugins and their dependencies.
     *
     * @parameter default-value="${project.remoteArtifactRepositories}"
     * @readonly
     */
    private List<RemoteRepository> remoteRepositories;
    /**
     * @component
     */
    protected ArtifactResolver resolver;


    /**
     * The target coordinates
     *
     * @parameter property="artifactCoords"
     * @required
     */
    private String artifactCoords;

    public void execute()
            throws MojoExecutionException, MojoFailureException {
        Artifact artifact;
        try {
            artifact = new DefaultArtifact(artifactCoords);
        } catch (IllegalArgumentException e) {
            throw new MojoFailureException(e.getMessage());
        }
        DefaultRepositorySystemSession session = new DefaultRepositorySystemSession(repoSession)
                .setUpdatePolicy(RepositoryPolicy.UPDATE_POLICY_ALWAYS)
                .setChecksumPolicy(RepositoryPolicy.CHECKSUM_POLICY_FAIL);
        ArtifactRequest request = new ArtifactRequest();
        LocalRepository localRepo = new LocalRepository("target/local-repo");
        session.setLocalRepositoryManager(repoSystem.newLocalRepositoryManager(repoSession, localRepo));
        request.setArtifact(artifact);

        request.setRepositories(remoteRepositories);

        getLog().info("Check connection to remote repositories " + remoteRepositories + " with artifact " + artifact);

        ArtifactResult result;
        try {
            result = repoSystem.resolveArtifact(session, request);
        } catch (ArtifactResolutionException e) {
            getLog().error("Connection to repository failed");
            throw new MojoExecutionException(e.getMessage(), e);
        }

        getLog().info("Resolved artifact " + artifact + " to " + result.getArtifact().getFile() + " from "
                + result.getRepository());

    }

}
