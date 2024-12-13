// tests/NavBar.spec.ts
import { mount } from '@vue/test-utils';
import NavBar from '../../src/components/NavBar.vue';
import { router } from '../../src/router';

describe('NavBar.vue', () => {
  it('renders the NavBar component', async () => {
    await router.push('/');
    await router.isReady();
    const wrapper = mount(NavBar, {
      global: {
        plugins: [router],
      },
    });
    expect(wrapper.exists()).toBe(true);
  });
});