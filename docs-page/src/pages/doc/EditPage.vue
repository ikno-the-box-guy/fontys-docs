<script setup lang="ts">
import {useRoute} from "vue-router";
import {onBeforeMount, onBeforeUnmount, onMounted, ref, watch} from "vue";
import { ArrowDownTrayIcon } from "@heroicons/vue/24/outline";
import MarkdownRenderer from "../../components/MarkdownRenderer.vue";
import {WebsocketProvider} from "y-websocket";
import * as Y from "yjs";

const route = useRoute();
const documentId = ref(route.params.file);

const markdown = ref('# Hello, World!');

const ydoc = ref<Y.Doc>();
const ytext = ref<Y.Text>();
const yprovider = ref<WebsocketProvider>();

const initializeWsConnection = () => {
  const doc = new Y.Doc();
  
  const provider = new WebsocketProvider(
      import.meta.env.VITE_WS_URL,
      documentId.value,
      doc,
      {maxBackoffTime: 1000, resyncInterval: 10000}
  )
  
  provider.on('status', (event) => {
    console.log(event.status); // logs "connected" or "disconnected"
  });
  
  const text = doc.getText('content');
  
  text.observe(onMarkdownServerChange);
  markdown.value = text.toString();
  
  ydoc.value = doc;
  ytext.value = text;
  yprovider.value = provider;
}

// Sync changes from the socket to the client
const onMarkdownServerChange = (event) => {
  markdown.value = event.currentTarget.toString();
}

// Sync changes from the client to the socket
const onMarkdownChange = () => {
  if (ytext.value) {
    // Make sure changes are atomic
    ydoc.value.transact(() => {
      ytext.value.delete(0, ytext.value.length);
      ytext.value.insert(0, markdown.value || "");
    });
  }
}

onMounted(() => {
  initializeWsConnection();
});

onBeforeUnmount(() => {
  // Cleanup
  if (ytext.value) {
    ytext.value.unobserve(onMarkdownServerChange);
  }
  
  if (ydoc.value) {
    ydoc.value.destroy();
  }
  
  if (yprovider.value) {
    yprovider.value.destroy();
  }
});

const saveDocument = () => {
  const blob = new Blob([markdown.value], { type: "text/markdown" });
  const url = URL.createObjectURL(blob);

  const link = document.createElement("a");
  link.href = url;
  link.download = documentId.value + ".md";
  link.click();
  
  link.remove()

  URL.revokeObjectURL(url);
}
</script>

<template>
  <div class="p-6 min-h-full xl:px-32">
    <div class="flex flex-row justify-between items-baseline">
      <h1 class="text-3xl font-bold mb-4">Edit</h1>
      <button @click="saveDocument">
        <ArrowDownTrayIcon class="h-6 w-6 text-gray-500"/>
      </button>
    </div>
    
    <div class="flex flex-col md:flex-row w-full space-y-4 md:space-x-4 md:space-y-0">
      <textarea v-model="markdown" class="flex-col w-full md:w-1/2 rounded-lg border-2 border-gray-300 p-4 h-96 bg-transparent focus-visible:outline-gray-400" @input="onMarkdownChange"></textarea>
      <div class="flex-col w-full md:w-1/2 rounded-lg border-2 border-gray-300 p-4">
        <article class="prose prose-img:rounded-lg prose-h1:mb-1 prose-a:text-blue-600 prose-img:shadow-lg">
          <MarkdownRenderer :source="markdown"/>
        </article>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>