# Releasing

1. Change the repo's metadata to reflect the impending release 
   - Update gradle.properties to a non-SNAPSHOT version.
   - Update CHANGELOG.md for the impending release.
   - Update README.md with the new version.
2. `git commit -am "Prepare for release X.Y.Z"` (where X.Y.Z is the new version)
3. Open a Pull Request with the above changes. Get it merged.
4. Create a tag for this version
   - `git tag -a X.Y.Z -m "Version X.Y.Z"` (where X.Y.Z is the version)
   - Push this tag to GitHub: `git push && git push --tags`

5. Someone with the necessary permissions publishes the repo:
   - `./gradlew clean publish`
   - Visit [Sonatype Central](https://central.sonatype.com/publishing), wait for the "pending" artifacts to become validated, and finally publish the artifacts.
If this step fails: drop the deployment, fix the problem, commit, and start again.
Visit [Maven Central Repository Search](https://search.maven.org/search?q=magellan) to verify the artifact is live. Note that it may take a few hours.

6. Visit [the GitHub releases page](https://github.com/wealthfront/magellan/releases) and create a new release, copying the changelog from CHANGELOG.md.
7. Change the repo's metadata to reflect the next development cycle 
   - Change gradle.properties to the next SNAPSHOT version.
   - `git commit -am "Prepare next development version"`
8. Open a Pull Request with the above changes. Get it merged.

## Publish to local maven repo

1. Set up [signatory credentials](https://docs.gradle.org/current/userguide/signing_plugin.html#sec:signatory_credentials), or temporarily comment out the `signing` block in `gradle/gradle-mvn-push.gradle`.
2. Run `./gradlew publishToMavenLocal`.
3. In the other project, add `mavenLocal()` as a repository (likely in `allProjects.repositories` of the root `build.gradle` file).
4. Update `com.wealthfront:magellan:X.Y.Z` to `com.wealthfront:magellan-library:SNAPSHOT_VERSION`, where `SNAPSHOT_VERSION` is the `VERSION_NAME` defined in this project's `./gradle.properties`.
