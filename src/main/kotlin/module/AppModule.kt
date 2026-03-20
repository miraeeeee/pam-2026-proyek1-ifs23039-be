package org.delcom.module

import org.delcom.repositories.*
import org.delcom.services.*
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single<IUserRepository> { UserRepository() }
    single<IRefreshTokenRepository> { RefreshTokenRepository() }
    single<IMatchRepository> { MatchRepository() }
    single<IPlayerStatRepository> { PlayerStatRepository() }

    // Services
    single { AuthService(get(), get()) }
    single { MatchService(get()) }
    single { PlayerStatService(get()) }
}
