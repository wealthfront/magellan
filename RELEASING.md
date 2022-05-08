# Releasing

1. Change the version in `gradle.properties` to a non-SNAPSHOT version.
2. Update the `CHANGELOG.md` for the impending release.
3. Update the `README.md` with the new version.
4. `git commit -am "Release X.Y.Z"` (where X.Y.Z is the new version)
5. `git tag -a X.Y.Z -m "Version X.Y.Z"` (where X.Y.Z is the new version)
6. Update the `gradle.properties` to the next SNAPSHOT version.
7. `git commit -am "Prepare next development version"`
8. `git push && git push --tags`
9. Visit [the GitHub releases page](https://github.com/ryanmoelter/magellanx/releases) and create a
   new release, copying the changelog from `CHANGELOG.md`.
10. Visit [the Jitpack site](https://jitpack.io/#com.ryanmoelter/magellanx) and check that the new
    release shows up

## Publish to local maven repo

1. Run `./gradlew publishToMavenLocal`.
2. In the other project, add `mavenLocal()` as a repository (likely in `allProjects.repositories` of
   the root `build.gradle` file).
3. Update `com.ryanmoelter:magellanx-compose:X.Y.Z`
   to `com.ryanmoelter:magellanx-compose:SNAPSHOT_VERSION`, where `SNAPSHOT_VERSION` is
   the `VERSION_NAME` defined in this project's `./gradle.properties`.
