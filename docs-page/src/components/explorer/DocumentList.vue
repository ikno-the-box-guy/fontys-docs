<script setup lang="ts">
import {Document} from "../../entities/Document.ts";

const props = defineProps<{
  documents: Document[];
}>();
</script>

<template>
  <ul class="space-y-2">
    <li v-for="doc in documents" :key="doc.id">
      <RouterLink :to="'/document/' + doc.id" class="text-blue-600 hover:underline">
        <div class="bg-white p-4 rounded-lg shadow hover:bg-gray-50 transition-colors">
          {{ doc.name}}
        </div>
      </RouterLink>
    </li>
  </ul>
</template>

<style scoped>

</style>