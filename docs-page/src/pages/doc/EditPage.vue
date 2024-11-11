<script setup lang="ts">
import {useRoute} from "vue-router";
import {ref, watch} from "vue";
import MarkdownRenderer from "../../components/MarkdownRenderer.vue";

const route = useRoute();
const documentId = ref(route.params.file);

const markdown = ref('# Hello, World!');
</script>

<template>
  <div class="p-6 bg-white min-h-full">
    <h1 class="text-3xl font-bold mb-4">Edit</h1>
    
    <div class="flex flex-col md:flex-row w-full space-y-4 md:space-x-4 md:space-y-0">
      <textarea v-model="markdown" class="flex-col w-full md:w-1/2 rounded-lg border-2 p-4 h-96 bg-transparent"></textarea>
      <div class="flex-col w-full md:w-1/2 rounded-lg border-2 p-4">
        <article class="prose prose-img:rounded-lg prose-h1:mb-1 prose-a:text-blue-600 prose-img:shadow-lg">
          <MarkdownRenderer :source="markdown"/>
        </article>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>