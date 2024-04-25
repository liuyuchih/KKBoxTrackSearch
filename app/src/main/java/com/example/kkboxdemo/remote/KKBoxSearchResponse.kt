package com.example.myapplication.remote

// High-level response structure
data class KKBoxSearchResponse(
    val tracks: TracksData,
    val summary: Summary,
    val paging: Paging
)

data class TracksData(
    val data: List<Track>
)

data class Track(
    val id: String,
    val name: String,
    val duration: Int,
    val isrc: String,
    val url: String,
    val track_number: Int,
    val explicitness: Boolean,
    val available_territories: List<String>,
    val album: Album
)

data class Album(
    val id: String,
    val name: String,
    val url: String,
    val explicitness: Boolean,
    val available_territories: List<String>,
    val release_date: String,
    val images: List<Image>,
    val artist: Artist
)

data class Image(
    val height: Int,
    val width: Int,
    val url: String
)

data class Artist(
    val id: String,
    val name: String,
    val url: String,
    val images: List<Image>
)

data class Summary(
    val total: Int
)

data class Paging(
    val offset: Int,
    val limit: Int,
    val previous: String?, // Could be null
    val next: String
)

