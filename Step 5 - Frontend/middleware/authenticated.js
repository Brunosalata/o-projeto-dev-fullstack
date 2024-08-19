export default function ({ store, redirect }) {
    let auth = store.getters['auth/isAuthenticated'];
    if(!auth) {
      return redirect('/login');
    }
  }