<template>
  <el-container class="full-height">
    <el-aside width="250px" class="history-container">
      <!-- <el-scrollbar class="history-scrollbar"> -->
        <el-button @click="createNewChat" type="primary" class="newchat">开始新聊天</el-button>
        <el-menu :default-active="currentSessionId" @select="handleSelect" class="history-menu">
          <el-menu-item v-for="history in chatHistories" :key="history" :index="history">
            {{ history }}
          </el-menu-item>
        </el-menu>
      <!-- </el-scrollbar> -->
    </el-aside>

    <el-container class="chat-container">
      <el-main class="chat-main">
        <!-- <el-scrollbar class="chat-scrollbar"> -->
          <div v-if="chats.length" class="chat-messages">
            <div v-for="chat in chats" :key="chat.cid" class="chat-item">
              <p class="user-message"><strong>User:</strong> {{ chat.question }}</p>
              <p class="ai-message" v-if="chat.answer"><strong>AI:</strong> {{ chat.answer }}</p>
            </div>
          </div>
          <div v-else class="no-chat">
            <p>欢迎与BlueLM对话</p>
          </div>
        <!-- </el-scrollbar> -->
      </el-main>

      <el-footer class="chat-footer">
        <el-input v-model="newQuestion" placeholder="输入你的消息" @keyup.enter="sendChat" class="chat-input">
          <el-button slot="prepend" icon="el-icon-microphone" @click="startVoiceInput"></el-button>
          <el-button slot="append" type="primary" @click="sendChat">发送</el-button>
        </el-input>
      </el-footer>
    </el-container>
  </el-container>
</template>

<script>
import { fetchhistory, fetchchat, sendquestion, newchat } from "@/api/chat"; // Change the path accordingly

export default {
  data() {
    return {
      chatHistories: [],
      chats: [],
      currentSessionId: null,
      newQuestion: "",
    };
  },
  mounted() {
    this.handleInitialPageLoad();
  },
  methods: {
    async handleInitialPageLoad() {
      const sessionIdFromURL = this.$route.query.sessionid;
      if (sessionIdFromURL) {
        this.currentSessionId = sessionIdFromURL;
        await this.loadChatHistories();
        await this.loadChat(this.currentSessionId);
      } else {
        await this.loadChatHistories();
      }
    },
    async loadChatHistories() {
      try {
        const response = await fetchhistory();
        this.chatHistories = response.data;
      } catch (error) {
        console.error("Error fetching chat histories:", error);
      }
    },
    async loadChat(sessionid) {
      try {
        const response = await fetchchat(sessionid);
        this.chats = response.data;
      } catch (error) {
        console.error("Error fetching chat:", error);
      }
    },
    async createNewChat() {
      if (this.$route.query.sessionid) {
        this.$router.push('/ai/chat');
        this.chats = [];
        this.currentSessionId = null
        await this.loadChatHistories();
      }
    },
    async sendChat() {
      if (!this.newQuestion.trim()) return;
      try {
        if (this.currentSessionId) {
          await sendquestion(this.newQuestion, this.currentSessionId);
          this.newQuestion = '';
          await this.loadChat(this.currentSessionId);
        } else {
          const response = await newchat(this.newQuestion);
          this.currentSessionId = response.data.sessionid;
          this.$router.push({ path: '/ai/chat/', query: { sessionid: this.currentSessionId } });
          this.newQuestion = '';
          await this.handleInitialPageLoad();
        }
      } catch (error) {
        console.error("Error sending chat:", error);
      }
    },
    async startVoiceInput() {
      // Implement voice input functionality if needed
    },
    handleSelect(sessionid) {
      this.currentSessionId = sessionid;
      this.$router.push({ path: '/ai/chat', query: { sessionid } });
      this.loadChat(sessionid);
    }
  }
};
</script>

<style scoped>
.full-height {
  height: calc(100vh - 60px);
  margin-top: 60px;
  display: flex;
}

.history-container {
  background-color: #2c3e50;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.history-scrollbar {
  height: calc(100vh - 60px);
  padding: 20px;
  box-sizing: border-box;
}

.newchat {
  width: 100%;
  margin-bottom: 20px;
}

.history-menu {
  margin-top: 20px;
  background-color: #34495e;
  border: none;
}

.history-menu .el-menu-item {
  color: #ecf0f1;
}

.history-menu .el-menu-item:hover,
.history-menu .el-menu-item.is-active {
  background-color: #1abc9c;
  color: #ecf0f1;
}

.chat-container {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.chat-main {
  padding: 20px;
  background-color: #ecf0f1;
  overflow-y: auto;
  box-sizing: border-box;
  flex: 1;
}

.chat-scrollbar {
  height: 100%;
}

.chat-messages {
  display: flex;
  flex-direction: column;
}

.chat-item {
  margin-bottom: 15px;
  border-radius: 5px;
  padding: 10px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-message {
  background-color: #3498db;
  color: #fff;
  padding: 10px;
  border-radius: 5px;
}

.ai-message {
  background-color: #bdc3c7;
  padding: 10px;
  border-radius: 5px;
  margin-top: 5px;
}

.no-chat {
  text-align: center;
  margin-top: 20px;
  color: #999;
}

.chat-footer {
  background-color: #ecf0f1;
  padding: 10px 20px;
}

.chat-input {
  width: 100%;
}
</style>
