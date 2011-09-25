#!/bin/sh

gen_icon() {
    file_name=$1
    img_size=$2
    res_folder=$3
    
    echo Converting misc/$file_name.svg to res/$res_folder/$file_name.png
    convert -resize $img_size -background transparent misc/$file_name.svg res/$res_folder/$file_name.png 
}

gen_app_icon() {
    file_name=$1
    
    gen_icon $file_name 72x72 drawable-hdpi
    gen_icon $file_name 48x48 drawable
    gen_icon $file_name 36x36 drawable-ldpi
}

gen_action_bar_icon() {
    file_name=$1
    
    gen_icon $file_name 48x48 drawable-hdpi
    gen_icon $file_name 32x32 drawable
    gen_icon $file_name 24x24 drawable-ldpi
}

gen_list_icon() {
    file_name=$1
    
    gen_icon $file_name 48x48 drawable-hdpi
    gen_icon $file_name 32x32 drawable
    gen_icon $file_name 24x24 drawable-ldpi
}

gen_app_icon icon
gen_action_bar_icon plus
gen_action_bar_icon plus_folder
gen_action_bar_icon ok
gen_action_bar_icon edit
gen_list_icon collapsed
gen_list_icon expanded
gen_list_icon empty
