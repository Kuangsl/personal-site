<script setup lang="ts">
</script>
<template>
    <n-config-provider>
        <n-message-provider>
            <n-dialog-provider>
                <n-global-style />
                <router-view />
            </n-dialog-provider>
        </n-message-provider>
    </n-config-provider>
</template>
<style>
/* 确保高度撑满 */
html,
body,
#app {
    height: 100%;
    margin: 0;
    padding: 0;
    background-color: #f5f7f9;
}
</style>