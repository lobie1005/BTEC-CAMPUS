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
<<<<<<< HEAD

=======
>>>>>>> lobi_ver
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
<<<<<<< HEAD
<<<<<<< HEAD
=======
<<<<<<< Updated upstream
>>>>>>> origin/demo1
        maven("https://jitpack.io") // Ensure JitPack is included
=======
        maven("https://jitpack.io") // JitPack repository
>>>>>>> Stashed changes
    }
}

rootProject.name = "Campus_Expense_Manager"
<<<<<<< Updated upstream
include(":app")

=======
    }
}

>>>>>>> lobi_ver
rootProject.name = "Campus Expense Management"
include(":app")
 
=======
include(":app")
>>>>>>> Stashed changes
