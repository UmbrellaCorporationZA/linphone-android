/*
 * Copyright (c) 2010-2023 Belledonne Communications SARL.
 *
 * This file is part of linphone-android
 * (see https://www.linphone.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.linphone.utils

import androidx.emoji2.text.EmojiCompat
import java.util.Locale
import org.linphone.LinphoneApplication.Companion.coreContext
import org.linphone.core.Address
import org.linphone.core.Call
import org.linphone.core.ChatRoom
import org.linphone.core.tools.Log

class LinphoneUtils {
    companion object {
        fun getFirstLetter(displayName: String): String {
            return getInitials(displayName, 1)
        }

        fun getInitials(displayName: String, limit: Int = 2): String {
            if (displayName.isEmpty()) return ""

            val split = displayName.uppercase(Locale.getDefault()).split(" ")
            var initials = ""
            var characters = 0
            val emoji = coreContext.emojiCompat

            for (i in split.indices) {
                if (split[i].isNotEmpty()) {
                    try {
                        if (emoji?.loadState == EmojiCompat.LOAD_STATE_SUCCEEDED && emoji.hasEmojiGlyph(
                                split[i]
                            )
                        ) {
                            val glyph = emoji.process(split[i])
                            if (characters > 0) { // Limit initial to 1 emoji only
                                Log.d("[App Utils] We limit initials to one emoji only")
                                initials = ""
                            }
                            initials += glyph
                            break // Limit initial to 1 emoji only
                        } else {
                            initials += split[i][0]
                        }
                    } catch (ise: IllegalStateException) {
                        Log.e("[App Utils] Can't call hasEmojiGlyph: $ise")
                        initials += split[i][0]
                    }

                    characters += 1
                    if (characters >= limit) break
                }
            }
            return initials
        }

        fun getDisplayName(address: Address?): String {
            if (address == null) return "[null]"
            if (address.displayName == null) {
                val account = coreContext.core.accountList.find { account ->
                    account.params.identityAddress?.asStringUriOnly() == address.asStringUriOnly()
                }
                val localDisplayName = account?.params?.identityAddress?.displayName
                // Do not return an empty local display name
                if (localDisplayName != null && localDisplayName.isNotEmpty()) {
                    return localDisplayName
                }
            }
            // Do not return an empty display name
            return address.displayName ?: address.username ?: address.asString()
        }

        fun isCallOutgoing(callState: Call.State): Boolean {
            return when (callState) {
                Call.State.OutgoingInit, Call.State.OutgoingProgress, Call.State.OutgoingRinging, Call.State.OutgoingEarlyMedia -> true
                else -> false
            }
        }

        private fun getChatRoomId(localAddress: Address, remoteAddress: Address): String {
            val localSipUri = localAddress.clone()
            localSipUri.clean()
            val remoteSipUri = remoteAddress.clone()
            remoteSipUri.clean()
            return "${localSipUri.asStringUriOnly()}~${remoteSipUri.asStringUriOnly()}"
        }

        fun getChatRoomId(chatRoom: ChatRoom): String {
            return getChatRoomId(chatRoom.localAddress, chatRoom.peerAddress)
        }
    }
}
