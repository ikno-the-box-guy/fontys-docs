import HomePage from "./pages/HomePage.vue";
import Explorer from "./pages/explorer/Explorer.vue";
import LoginPage from "./pages/auth/LoginPage.vue";
import RegisterPage from "./pages/auth/RegisterPage.vue";
import {createRouter, createWebHistory} from "vue-router";
import user from "./utils/localUser.ts";
import PageNotFound from "./pages/PageNotFound.vue";
import EditPage from "./pages/doc/EditPage.vue";

const routes = [
    {
        name: 'home',
        path: '/',
        component: HomePage,
        meta: {
            requiresAuth: true
        }
    },
    {
        name: 'explorer',
        path: '/explorer/:directory',
        component: Explorer,
        meta: {
            requiresAuth: true
        }
    },
    {
        name: 'edit',
        path: '/edit/:file',
        component: EditPage,
        meta: {
            requiresAuth: true
        }
    },
    { 
        name: 'login', 
        path: '/login', 
        component: LoginPage 
    },
    { 
        name: 'register', 
        path: '/register', 
        component: RegisterPage
    },
    {
        name: '404',
        path: '/:pathMatch(.*)*',
        component: PageNotFound
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!user.value) {
            next({
                name: 'login'
            })
        } else {
            if (user.value.expiration < Date.now()) {
                user.value = null
                next({
                    name: 'login'
                })
            }
            next()
        }
    } else {
        next()
    }
});

export default router