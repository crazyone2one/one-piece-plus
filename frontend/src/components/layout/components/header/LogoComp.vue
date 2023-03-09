<template>
  <div class="ui-logo">
    <img src="../../../../assets/one-piece-logo.svg" />
    <span> One Piece</span>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped>
.ui-logo {
  cursor: pointer;
  display: flex;
  font-size: 18px;
}
.ui-logo {
  align-items: center;
}
.ui-logo > img {
  height: 32px;
  width: 32px;
  margin-right: 12px;
}
img {
  overflow-clip-margin: content-box;
  overflow: clip;
  overflow-x: 0;
  overflow-y: 0;
}
</style>
