<script setup lang="ts">
  import Popper from "vue3-popper";
  import user from "../utils/localUser.ts";
  import { router } from "../router.ts";
  
  const handleLogout = () => {
    user.value = null;
    router.push('/login');
  }
</script>

<template>
  <Popper>
    <button>{{user.name}}</button>
    <template #content>
      <div class="bg-white border-2 rounded p-4 gap-3 flex flex-col text-black">
        <p>{{user.name}}</p>
        <p>{{user.email}}</p>
        <button @click="handleLogout" class="btn btn-red">Logout</button>
      </div>
    </template>
  </Popper>
</template>

<style scoped>

</style>