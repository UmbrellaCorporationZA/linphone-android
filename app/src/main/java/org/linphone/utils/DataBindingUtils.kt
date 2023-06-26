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

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import org.linphone.R
import org.linphone.contacts.ContactData

/**
 * This file contains all the data binding necessary for the app
 */

@BindingAdapter("android:src")
fun ImageView.setSourceImageResource(resource: Int) {
    this.setImageResource(resource)
}

@BindingAdapter("android:textStyle")
fun TextView.setTypeface(typeface: Int) {
    this.setTypeface(null, typeface)
}

@BindingAdapter("coilContact")
fun loadContactPictureWithCoil(imageView: ImageView, contact: ContactData?) {
    if (contact == null) {
        imageView.load(R.drawable.contact_avatar)
    } else {
        imageView.load(contact.avatar) {
            transformations(CircleCropTransformation())
            error(R.drawable.contact_avatar)
        }
    }
}
