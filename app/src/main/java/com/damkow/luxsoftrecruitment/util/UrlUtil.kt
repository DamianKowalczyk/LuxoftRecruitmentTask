package com.damkow.luxsoftrecruitment.util

class UrlUtil {

    companion object {
        fun videoImageUrl(videoPath: String): String {
            return "https://image.tmdb.org/t/p/original${videoPath}"
        }
    }
}