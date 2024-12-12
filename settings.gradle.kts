pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
<<<<<<< Updated upstream
        maven("https://jitpack.io") // Ensure JitPack is included
=======
        maven("https://jitpack.io") // JitPack repository
>>>>>>> Stashed changes
    }
}

rootProject.name = "Campus_Expense_Manager"
<<<<<<< Updated upstream
include(":app")

rootProject.name = "Campus Expense Management"
include(":app")
 
=======
include(":app")
>>>>>>> Stashed changes
