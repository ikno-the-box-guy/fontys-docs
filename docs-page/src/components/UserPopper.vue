<script setup lang="ts">
  import Popper from "vue3-popper";
  import user from "../utils/localUser.ts";
</script>

<template>
  <Popper>
    <button>{{user.name}}</button>
    <template #content>
      <div class="bg-white border-2 rounded p-4">
        <p>{{user.name}}</p>
        <p>{{user.email}}</p>
      </div>
    </template>
  </Popper>
</template>

<style scoped>

</style>