<template>
  <div class="app-container">
    <el-card shadow="hover">
      <div slot="header"><span>我的任务</span></div>

      <div class="task-list">
        <div class="task-item" v-for="(item, index) in list" :key="index">
          <div class="level-tag" :style="getLevelColor(item.levelMark)">
            {{ item.levelMark }}
          </div>
          <div class="task-info">
            <p class="address">{{ item.province }} {{ item.city }} {{ item.date }}</p>
            <p class="detail">{{ item.address }}</p>
          </div>
          <el-button type="primary" class="detect-btn" @click="goDetect(item)">
            去检测
            <span class="badge">{{ index + 1 }}</span>
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "WorkerTask",
  data() {
    return {
      list: [
        { id: 1, levelMark: "二", province: "辽宁省", city: "沈阳市", date: "2026-4-10", address: "大东区岩泉路456号", state: 1 },
        { id: 2, levelMark: "四", province: "辽宁省", city: "沈阳市", date: "2026-4-11", address: "和平区建设路342号", state: 1 },
        { id: 3, levelMark: "五", province: "辽宁省", city: "大连市", date: "2026-4-12", address: "甘井子区和平路667号", state: 1 }
      ]
    };
  },
  methods: {
    getLevelColor(mark) {
      const colorMap = {
        "一": "#02E300",
        "二": "#FFFF00",
        "三": "#FF7E00",
        "四": "#FE0000",
        "五": "#98004B",
        "六": "#7E0123"
      };
      return {
        backgroundColor: colorMap[mark] || "#fff",
        color: "#fff"
      };
    },
    goDetect(row) {
      this.$router.push({
        path: "/worker/detect",
        query: {
          taskId: row.id,
          levelMark: row.levelMark,
          address: row.address,
          province: row.province,
          city: row.city
        }
      });
    }
  }
};
</script>

<style scoped>
.task-list { padding: 10px 0; }
.task-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  margin-bottom: 12px;
}
.level-tag {
  width: 40px;
  height: 40px;
  border: 1px solid #333;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  margin-right: 16px;
}
.task-info { flex: 1; }
.address { font-size: 18px; margin: 0 0 6px 0; color: #333; }
.detail { font-size: 16px; margin: 0; color: #666; }
.detect-btn { position: relative; }
.badge {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #ff4d4f;
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 10px;
}
</style>
