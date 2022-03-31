package org.captaindmitro.domain.usecases

import org.captaindmitro.domain.repositories.Repository

class GetCuratedPhotosUseCase constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.getCuratedPhotos()
}