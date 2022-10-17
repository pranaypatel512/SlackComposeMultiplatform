package dev.baseio.slackclone.uichannels

import dev.baseio.slackdomain.model.channel.DomainLayerChannels
import dev.baseio.slackdomain.model.workspaces.DomainLayerWorkspaces
import dev.baseio.slackdomain.usecases.channels.UseCaseFetchRecentChannels
import dev.baseio.slackdomain.usecases.channels.UseCaseFetchAllChannels
import dev.baseio.slackdomain.usecases.workspaces.UseCaseGetSelectedWorkspace
import kotlinx.coroutines.flow.*
import ViewModel

class SlackChannelVM constructor(
  private val ucFetchChannels: UseCaseFetchAllChannels,
  private val useCaseGetSelectedWorkspace: UseCaseGetSelectedWorkspace,
  private val ucFetchRecentChannels: UseCaseFetchRecentChannels
) : ViewModel() {

  val channels = MutableStateFlow<Flow<List<DomainLayerChannels.SKChannel>>>(emptyFlow())

  fun allChannels() {
    channels.value = useCaseGetSelectedWorkspace.invokeFlow().flatMapLatest {
      fetchChannels(it)
    }
  }

  fun loadDirectMessageChannels() {
    channels.value =
      useCaseGetSelectedWorkspace.invokeFlow().flatMapLatest {
        fetchChannels(it)
      }
  }

  fun loadRecentChannels() {
    channels.value =
      useCaseGetSelectedWorkspace.invokeFlow().flatMapLatest {
        recentChannels(it)
      }
  }

  private fun recentChannels(it: DomainLayerWorkspaces.SKWorkspace?) =
    ucFetchRecentChannels(it!!.uuid)


  fun loadStarredChannels() {
    channels.value =
      useCaseGetSelectedWorkspace.invokeFlow().flatMapLatest {
        fetchChannels(it)
      }

  }

  private fun fetchChannels(it: DomainLayerWorkspaces.SKWorkspace?) =
    ucFetchChannels(it!!.uuid)


}