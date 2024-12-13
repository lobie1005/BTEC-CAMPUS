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
        maven("https://jitpack.io") // Ensure JitPack is included
    }
}

rootProject.name = "Campus_Expense_Manager"
include(":app")

=======
    }
}

>>>>>>> lobi_ver
rootProject.name = "Campus Expense Management"
include(":app")
 