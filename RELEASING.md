# Releasing

 1. Change the version in `gradle.properties` to a non-SNAPSHOT version.
 2. Update the `CHANGELOG.md` for the impending release.
 3. Update the `README.md` with the new version.
 4. `git commit -am "Prepare for release X.Y.Z."` (where X.Y.Z is the new version)
 5. `git tag -a X.Y.Z -m "Version X.Y.Z"` (where X.Y.Z is the new version)
 6. `./gradlew clean publish`
 7. Update the `gradle.properties` to the next SNAPSHOT version.
 8. `git commit -am "Prepare next development version."`
 9. `git push && git push --tags`
 10. Visit [Sonatype Nexus](https://oss.sonatype.org/) and promote the artifact.

If step 6 or 7 fails, drop the Sonatype repo, fix the problem, commit, and start again at step 5.

## Publish to local maven repo

1. Set up [signatory credentials](https://docs.gradle.org/current/userguide/signing_plugin.html#sec:signatory_credentials), or temporarily comment out the `signing` block in `gradle/gradle-mvn-push.gradle`.
2. Run `./gradlew publishToMavenLocal`.
3. In the other project, add `mavenLocal()` as a repository (likely in `allProjects.repositories` of the root `build.gradle` file).
4. Update `com.wealthfront:magellan:X.Y.Z` to `com.wealthfront:magellan-library:SNAPSHOT_VERSION`, where `SNAPSHOT_VERSION` is the `VERSION_NAME` defined in this project's `./gradle.properties`.
