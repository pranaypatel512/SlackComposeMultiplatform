package dev.baseio.slackclone.uidashboard.vm

import dev.baseio.slackclone.appNavigator
import dev.baseio.slackclone.navigation.SlackScreens
import dev.baseio.slackdomain.model.users.DomainLayerUsers
import dev.baseio.slackdomain.usecases.auth.UseCaseLogout
import dev.baseio.slackdomain.usecases.auth.UseCaseCurrentUser
import io.github.timortel.kotlin_multiplatform_grpc_lib.KMCode
import io.github.timortel.kotlin_multiplatform_grpc_lib.KMStatusException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

interface UserProfileDelegate {
  val currentLoggedInUser: MutableStateFlow<DomainLayerUsers.SKUser?>
  fun getCurrentUser(viewModelScope: CoroutineScope)
  fun logout()
}

class UserProfileDelegateImpl(
  private val useCaseCurrentUser: UseCaseCurrentUser,
  private val useCaseClearAuth: UseCaseLogout,
) : UserProfileDelegate {
  override val currentLoggedInUser: MutableStateFlow<DomainLayerUsers.SKUser?> = MutableStateFlow(null)

  override fun logout() {
    useCaseClearAuth()
  }

  override fun getCurrentUser(viewModelScope: CoroutineScope) {
    viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
      when {
        throwable is KMStatusException && throwable.status.code == KMCode.UNAUTHENTICATED -> {
          useCaseClearAuth()
          appNavigator.navigateRoute(SlackScreens.OnboardingRoute, removeRoute = { route, remove ->
            if (route.name == SlackScreens.DashboardRoute.name) {
              remove()
            }
          })
        }
      }
    }) {
      val result = useCaseCurrentUser()
      currentLoggedInUser.value = result.getOrThrow()
    }
  }

}