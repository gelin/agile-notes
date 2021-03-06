#!/bin/sh

#   Script to generate png icons in different resolutions from svg sources.
#   Copyright (C) 2011  Denis Nelubin
#
#   This program is free software: you can redistribute it and/or modify
#   it under the terms of the GNU General Public License as published by
#   the Free Software Foundation, either version 3 of the License, or
#   (at your option) any later version.
#
#   This program is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#   GNU General Public License for more details.
#
#   You should have received a copy of the GNU General Public License
#   along with this program.  If not, see <http://www.gnu.org/licenses/>.

gen_icon() {
    file_name=$1
    img_size=$2
    res_folder=$3
    
    echo Converting misc/$file_name.svg to res/$res_folder/$file_name.png
    convert -resize $img_size -background transparent misc/$file_name.svg res/$res_folder/$file_name.png 
}

gen_app_icon() {
    file_name=$1
    
    gen_icon $file_name 96x96 drawable-xhdpi
    gen_icon $file_name 72x72 drawable-hdpi
    gen_icon $file_name 48x48 drawable
    gen_icon $file_name 36x36 drawable-ldpi
}

gen_logo_icon() {
    file_name=$1

    gen_icon $file_name 48x96 drawable-xhdpi
    gen_icon $file_name 36x72 drawable-hdpi
    gen_icon $file_name 24x48 drawable
    gen_icon $file_name 18x36 drawable-ldpi
}

gen_action_bar_icon() {
    file_name=$1
    
    gen_icon $file_name 48x48 drawable-xhdpi
    gen_icon $file_name 36x36 drawable-hdpi
    gen_icon $file_name 24x24 drawable
    gen_icon $file_name 18x18 drawable-ldpi
}

gen_list_icon() {
    file_name=$1

    gen_icon $file_name 48x48 drawable-hdpi
    gen_icon $file_name 32x32 drawable
    gen_icon $file_name 24x24 drawable-ldpi
}

gen_menu_icon() {
    file_name=$1

    gen_icon $file_name 48x48 drawable
}

gen_app_icon icon
gen_logo_icon logo
gen_action_bar_icon plus
gen_action_bar_icon plus_folder
gen_action_bar_icon ok
gen_action_bar_icon edit
gen_action_bar_icon progress0
gen_action_bar_icon progress1
gen_action_bar_icon progress2
gen_action_bar_icon progress3
gen_action_bar_icon progress4
gen_action_bar_icon progress5
gen_action_bar_icon progress6
gen_action_bar_icon progress7
gen_list_icon collapsed
gen_list_icon expanded
gen_list_icon empty
gen_list_icon folder
gen_list_icon drag_handle
gen_menu_icon plus_top
gen_menu_icon plus_bottom
