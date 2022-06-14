# Releasing

1a.) Change the version in gradle.properties to a non-SNAPSHOT version.
1b.) Update the CHANGELOG.md for the impending release.
1c.) Update the README.md with the new version.
2.) `git commit -am "Prepare for release X.Y.Z"` (where X.Y.Z is the new version)
3.) Open a Pull Request with the above changes. Get it merged
4a.) Create a tag for this version: `git tag -a X.Y.Z -m "Version X.Y.Z"` (where X.Y.Z is the version)
4b.) Push this tag to GitHub: `git push && git push --tags`

Someone with the necessary permissions publishes the repo:
5a.) `./gradlew clean publish`
5b.) Visit [Sonatype Nexus](https://oss.sonatype.org/) and promote the artifact.

6.) Visit [the GitHub releases page](https://github.com/wealthfront/magellan/releases) and create a new release, copying the changelog from CHANGELOG.md.
7a.) Change the gradle.properties to the next SNAPSHOT version.
7b.) git commit -am "Prepare next development version"
8.) Open a Pull Request with the above changes. Get it merged

Visit [Maven Central Repository Search](https://search.maven.org/search?q=magellan) to check when the artifact is live. Note: it may take a few hours.

If step 5 fails, drop the Sonatype repo, fix the problem, commit, and start again at step 4.

## Publish to local maven repo

1. Set up [signatory credentials](https://docs.gradle.org/current/userguide/signing_plugin.html#sec:signatory_credentials), or temporarily comment out the `signing` block in `gradle/gradle-mvn-push.gradle`.
2. Run `./gradlew publishToMavenLocal`.
3. In the other project, add `mavenLocal()` as a repository (likely in `allProjects.repositories` of the root `build.gradle` file).
4. Update `com.wealthfront:magellan:X.Y.Z` to `com.wealthfront:magellan-library:SNAPSHOT_VERSION`, where `SNAPSHOT_VERSION` is the `VERSION_NAME` defined in this project's `./gradle.properties`.
