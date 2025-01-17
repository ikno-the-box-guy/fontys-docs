// tests/router/HomeRouter.spec.ts
import {router} from '../../src/router';
import {afterEach, expect} from "vitest";
import localUser from "../../src/utils/localUser";

describe('HomeRoute.vue', () => {
    afterEach(() => {
        window.localStorage.clear();
    });
    
    it('redirects to login page if no user is set in localStorage', async () => {
        await router.push('/');
        await router.isReady();

        expect(router.currentRoute.value.name).toBe('login');
    });

    it('continues to home page if user is set in localStorage', async () => {
        localUser.value = {
            name: 'test',
            email: 'test@test.com',
            root: 1,
            expiration: Date.now() + 1000,
        };
        
        await router.push('/');
        await router.isReady();
        
        expect(router.currentRoute.value.name).toBe('home');
    });

    it('back to login if token expired', async () => {
        localUser.value = {
            name: 'test',
            email: 'test@test.com',
            root: 1,
            expiration: Date.now() - 1000,
        };

        await router.push('/');
        await router.isReady();

        expect(router.currentRoute.value.name).toBe('login');
    });
});