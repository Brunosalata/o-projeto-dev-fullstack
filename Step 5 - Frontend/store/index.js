export const actions = {
    async nuxtServerInit ({ dispatch, commit, state}) {
        const { accessToken, refreshToken } = state.auth
        if (accessToken && refreshToken) {
            try {
                await dispatch('auth/refresh')
            } catch (e) {
                await dispatch('auth/logout')
            }
        }
    },
}