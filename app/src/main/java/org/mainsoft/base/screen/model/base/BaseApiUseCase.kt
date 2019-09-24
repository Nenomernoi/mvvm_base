package org.mainsoft.base.screen.model.base

import org.mainsoft.base.net.Repository

abstract class BaseApiUseCase(protected val repository: Repository) : BaseUseCase()