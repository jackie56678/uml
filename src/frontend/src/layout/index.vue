<template>
  <div class="layout">
    <Sider :isCollapsed="isCollapsed" />
    <div :class="['main', { 'collapsed': isCollapsed }]">
      <Header :isCollapsed="isCollapsed" @toggle-collapse="handleToggleCollapse" />
      <div class="header-placeholder"></div>
      <div class="content">
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

<style scoped>
.layout {
  display: flex;
  height: 100vh;
}

.main {
  display: flex;
  flex-direction: column;
  flex: 1;
  transition: margin-left 0.3s;
  margin-left: 220px; /* 侧边栏全宽状态下的左边距 */
}

.main.collapsed {
  margin-left: 60px; /* 侧边栏收缩状态下的左边距 */
}

.header-placeholder {
  height: 60px; /* header的高度 */
}

.content {
  flex: 1;
  padding: 20px;
  background-color: #f5f5f5; /* 确保背景颜色覆盖整个区域 */
  overflow: auto;
  height: calc(100vh - 60px); /* 调整以适应header的高度 */
  box-sizing: border-box;
}
</style>
