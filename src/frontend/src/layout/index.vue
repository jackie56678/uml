<template>
  <div class="layout">
    <Sider :isCollapsed="isCollapsed" />
    <div class="main">
      <Header :isCollapsed="isCollapsed" @toggle-collapse="handleToggleCollapse" />
      <div class="header-placeholder"></div>
      <div :class="['content', { 'content-collapsed': isCollapsed }]">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
import Header from './Components/header.vue';
import Sider from './Components/sider.vue';

export default {
  components: {
    Header,
    Sider
  },
  data() {
    return {
      isCollapsed: false
    };
  },
  methods: {
    handleToggleCollapse() {
      this.isCollapsed = !this.isCollapsed;
    }
  }
};
</script>

<style>
.layout {
  display: flex;
  height: 100vh;
}

.main {
  display: flex;
  flex-direction: column;
  flex: 1;
  transition: margin-left 0.3s;
  margin-left: 220px; /* Adjust according to the full width of the sidebar */
}

.main.collapsed {
  margin-left: 60px; /* Adjust according to the collapsed width of the sidebar */
}

.header-placeholder {
  height: 60px; /* Height of the header */
}

.content {
  flex: 1;
  padding: 20px;
  transition: margin-left 0.3s;
}

.content-collapsed {
  margin-left: 60px; /* Adjust according to the collapsed width of the sidebar */
}

.content {
  margin-left: 220px; /* Adjust according to the full width of the sidebar */
}
</style>
