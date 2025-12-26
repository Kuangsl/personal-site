<script setup lang="ts">
  import { NConfigProvider, NMessageProvider } from 'naive-ui'
  </script>
  
  <template>
    <n-config-provider>
      <n-message-provider>
        
        <router-view />
        
      </n-message-provider>
    </n-config-provider>
  </template>
  
  <style>
  /* 确保占满全屏 */
  #app {
    width: 100%;
    height: 100vh;
    margin: 0;
    padding: 0;
  }
  </style>