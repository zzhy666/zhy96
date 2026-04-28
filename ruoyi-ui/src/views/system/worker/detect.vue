<template>
  <div class="app-container">
    <el-card shadow="hover">
      <div slot="header"><span>AQI检测上报</span></div>
      <div class="feedback-card">
        <div class="feedback-row">
          <span class="label">反馈用户</span>
          <span class="value">{{feedbackUser}}</span>
        </div>
        <div class="feedback-row">
          <span class="label">网格</span>
          <span class="value">{{ queryData.province }} {{ queryData.city }}</span>
        </div>
        <div class="feedback-row">
          <span class="label">详细地址</span>
          <span class="value">{{ queryData.address }}</span>
        </div>
        <div class="feedback-row">
          <span class="label">预估等级</span>
          <span class="value">{{ getEstimateLevel(levelMark) }}</span>
        </div>
        <div class="feedback-row">
          <span class="label">信息描述</span>
          <span class="value">{{feedbackDesc}}</span>
        </div>
      </div>
      <div class="input-section">
        <div class="input-row">
          <span class="input-label">SO2浓度</span>
          <el-input v-model.number="form.so2" placeholder="请输入浓度值"></el-input>
          <span class="unit">ug/m3</span>
          <div class="level-box" :style="so2Color">{{ so2Level }}</div>
        </div>
        <div class="input-row">
          <span class="input-label">CO浓度</span>
          <el-input v-model.number="form.co" placeholder="请输入浓度值"></el-input>
          <span class="unit">ug/m3</span>
          <div class="level-box" :style="coColor">{{ coLevel }}</div>
        </div>
        <div class="input-row">
          <span class="input-label">PM2.5浓度</span>
          <el-input v-model.number="form.pm25" placeholder="请输入浓度值"></el-input>
          <span class="unit">ug/m3</span>
          <div class="level-box" :style="pm25Color">{{ pm25Level }}</div>
        </div>
      </div>
      <div class="result-box" :style="aqiColor">
        实测AQI等级：{{ aqiLevel }}
      </div>
      <el-button type="primary" class="submit-btn" @click="submit">
        提交实测数据
      </el-button>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "WorkerDetect",
  data() {
    return {
      queryData: {
        province: "",
        city: "",
        address: ""
      },
      feedbackUser: "",
      feedbackDesc: "",
      levelMark: "",
      form: {
        so2: null,
        co: null,
        pm25: null
      },
      so2Level: "-",
      coLevel: "-",
      pm25Level: "-",
      so2Color: "",
      coColor: "",
      pm25Color: "",
      aqiLevel: "暂未检测",
      aqiColor: ""
    };
  },
  created() {
    if (this.$route.query) {
      this.queryData.province = this.$route.query.province ;
      this.queryData.city = this.$route.query.city ;
      this.queryData.address = this.$route.query.address ;
      this.levelMark = this.$route.query.levelMark ;
      this.feedbackUser = this.$route.query.feedbackUser ;
      this.feedbackDesc = this.$route.query.feedbackDesc ;
    }
  },
  watch: {
    "form.so2": "calcAqi",
    "form.co": "calcAqi",
    "form.pm25": "calcAqi"
  },
  methods: {
    getEstimateLevel(mark) {
      const map = {
        "一": "一级 优",
        "二": "二级 良",
        "三": "三级 轻度污染",
        "四": "四级 中度污染",
        "五": "五级 重度污染",
        "六": "六级 严重污染"
      };
      return map[mark] || "暂无预估等级";
    },
    getLevelText(level) {
      const map = { 1: "一", 2: "二", 3: "三", 4: "四", 5: "五", 6: "六" };
      return map[level] || "-";
    },
    getAqiName(level) {
      const map = {
        1: "一级 优",
        2: "二级 良",
        3: "三级 轻度污染",
        4: "四级 中度污染",
        5: "五级 重度污染",
        6: "六级 严重污染"
      };
      return map[level] || "暂未检测";
    },
    getColor(level) {
      const map = {
        1: "#02E300",
        2: "#FFFF00",
        3: "#FF7E00",
        4: "#FE0000",
        5: "#98004B",
        6: "#7E0123"
      };
      return map[level] || "#fff";
    },
    calcSo2(val) {
      if (val <= 50) return 1;
      if (val <= 150) return 2;
      if (val <= 475) return 3;
      if (val <= 800) return 4;
      if (val <= 1600) return 5;
      return 6;
    },
    calcCo(val) {
      if (val <= 5) return 1;
      if (val <= 10) return 2;
      if (val <= 35) return 3;
      if (val <= 60) return 4;
      if (val <= 90) return 5;
      return 6;
    },
    calcPm25(val) {
      if (val <= 35) return 1;
      if (val <= 75) return 2;
      if (val <= 115) return 3;
      if (val <= 150) return 4;
      if (val <= 250) return 5;
      return 6;
    },
    calcAqi() {
      const so2 = Number(this.form.so2) || 0;
      const co = Number(this.form.co) || 0;
      const pm25 = Number(this.form.pm25) || 0;
      const l1 = this.calcSo2(so2);
      const l2 = this.calcCo(co);
      const l3 = this.calcPm25(pm25);
      this.so2Level = this.getLevelText(l1);
      this.coLevel = this.getLevelText(l2);
      this.pm25Level = this.getLevelText(l3);
      this.so2Color = { backgroundColor: this.getColor(l1), color: "#fff" };
      this.coColor = { backgroundColor: this.getColor(l2), color: "#fff" };
      this.pm25Color = { backgroundColor: this.getColor(l3), color: "#fff" };
      const maxLevel = Math.max(l1, l2, l3);
      this.aqiLevel = this.getAqiName(maxLevel);
      this.aqiColor = {
        backgroundColor: this.getColor(maxLevel),
        color: "#fff",
        borderColor: this.getColor(maxLevel)
      };
    },
    submit() {
      this.$message.success("提交成功！");
      this.$router.push("/worker/task");
    }
  }
};
</script>

<style scoped>
.feedback-card {
  border: 1px solid #333;
  border-radius: 4px;
  margin-bottom: 20px;
}
.feedback-row {
  display: flex;
  padding: 12px 16px;
  border-bottom: 1px solid #333;
}
.feedback-row:last-child { border-bottom: none; }
.label { width: 100px; font-weight: bold; }
.value { flex: 1; }
.input-section { margin-bottom: 20px; }
.input-row {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}
.input-label { width: 100px; font-size: 16px; }
.unit { margin: 0 12px; }
.level-box {
  width: 40px;
  height: 40px;
  border: 1px solid #333;
  display: flex;
  align-items: center;
  justify-content: center;
}
.result-box {
  border: 1px solid #333;
  border-radius: 4px;
  padding: 16px;
  text-align: center;
  font-size: 18px;
  margin-bottom: 20px;
}
.submit-btn {
  width: 100%;
  height: 50px;
  font-size: 18px;
}
</style>
