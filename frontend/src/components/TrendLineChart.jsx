import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
} from "chart.js";

import { Line } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
);

function TrendLineChart({ data, label, valueKey = "amount" }) {
  const chartData = {
    labels: data.map((item) => item.label),

    datasets: [
      {
        label: label,
        data: data.map((item) => item[valueKey]),
        tension: 0.3,
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,

    plugins: {
      legend: {
        display: true,
      },
    },
  };

  return (
    <div
      style={{
        height: "280px",
        position: "relative",
      }}
    >
      <Line data={chartData} options={options} />
    </div>
  );
}

export default TrendLineChart;
