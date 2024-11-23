<script setup lang="ts">
import {useRoute} from "vue-router";
import {onBeforeMount, onBeforeUnmount, onMounted, ref, watch} from "vue";
import { ArrowDownTrayIcon, ArrowLeftIcon } from "@heroicons/vue/24/outline";
import MarkdownRenderer from "../../components/MarkdownRenderer.vue";
import {WebsocketProvider} from "y-websocket";
import * as Y from "yjs";
import {Document} from "../../entities/Document.ts";
import {documentApi} from "../../api/AxiosInstances.ts";

const route = useRoute();
const documentId = ref(route.params.file);

const markdown = ref('# Hello, World!');

const document = ref<Document>();

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
  loadDocument();
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

const loadDocument = () => {
  if (documentId.value) {
    documentApi.get("/documents/" + documentId.value)
        .then((response) => {
          document.value = response.data as Document;
          markdown.value = document.value.content;
        }).catch((error) => {
      console.error(error);
    });
    
  }
}
</script>

<template>
  <div class="p-6 min-h-full xl:px-32">
    
    <div class="flex flex-row text-gray-600 items-center w-full">
      <RouterLink
          v-if="document" :to="'/explorer/' + document?.parentId"
          class="mr-3"
      >
        <ArrowLeftIcon class="h-6 w-6 hover:text-blue-600"/>
      </RouterLink>
      <span v-if="document" class="text-2xl text-gray-800">{{ document.displayName }}</span>
      <button @click="saveDocument" class="ml-auto">
        <ArrowDownTrayIcon class="h-6 w-6 text-gray-500 hover:text-blue-700"/>
      </button>
    </div>
    <hr class="border-gray-300 my-2"/>
    
    <div class="flex flex-col md:flex-row w-full space-y-4 md:space-x-4 md:space-y-0">
      <div class="flex-col w-full md:w-1/2">
        <h1 class="text-3xl font-bold mb-4">
          Edit
        </h1>
        <textarea placeholder="Type your markdown here..." v-focus v-model="markdown" class="w-full rounded-lg border-2 border-gray-300 p-4 h-96 bg-transparent focus-visible:outline-gray-400" @input="onMarkdownChange"></textarea>
      </div>
      
      <div class="flex-col w-full md:w-1/2">
        <h1 class="w-full text-3xl font-bold mb-4">Preview</h1>
        
        <div class="w-full rounded-lg border-2 border-gray-300 p-4">
          <article class="prose prose-li:my-0 marker:text-gray-800 prose-img:rounded-lg prose-h1:mb-1 prose-a:text-blue-600 prose-img:shadow-lg">
            <MarkdownRenderer :source="markdown"/>
          </article>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>