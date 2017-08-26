package com.edusasse.lfpa.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.edusasse.lfpa.calculo.CalculoFPA;
import com.edusasse.lfpa.calculo.Constants;
import com.edusasse.lfpa.calculo.ControlaCalc;
import com.edusasse.lfpa.calculo.Facade;
import com.edusasse.lfpa.calculo.OuvintesCompilador;
import com.edusasse.lfpa.calculo.Resultado;


/**
 * 
 * @author Eduardo
 */
public class TelaGUI extends JFrame implements OuvintesCompilador {
	protected UndoManager undo = new UndoManager();

	// Classes internas
	private interface Listener {
		// método de atualização do componente
		public void update();
	}

	// Label de Status
	private class jLabelFileStatus extends JLabel implements Listener {
		// construtor
		public jLabelFileStatus(String text) {
			super(text);
			this.update();
		}

		// método de atualização do componente
		public void update() {
			if (fileStatus == 'M')
				this.setText("Modificado");
			else if (fileStatus == 'A')
				this.setText("Aberto");
			else if (fileStatus == 'S')
				this.setText("Salvo");
			else
				this.setText("Novo");
		}
	}

	// Label de Status
	private class jLabelFileName extends JLabel implements Listener {
		// construtor
		public jLabelFileName(String text) {
			super(text);
			this.update();
		}

		// método de atualização do componente
		public void update() {
			try {
				if (fileStatus != 'N')
					this.setText(caminhoArquivoAberto.getPath());
				else
					this.setText("");
			} catch (Exception e) {
				this.setText("");
			}
		}
	}

	// Facade
	private Facade controla;
	// Split dos paineis superior e inferior
	private JSplitPane jspPaineis;

	// Painel superior, que contera o codigo e a barra de tarefas
	private JPanel painelHead;
	// Painel que contera barra de status e mensages
	private JPanel painelFoot;
	// Painel que contera barra de status
	private JPanel painelStatus;

	// Area que contera o codigo fonte
	private JTextArea jtaEditor;
	private JScrollPane jscEditor;
	// Borda com os numeros
	private LineNumberedBorder lnb;

	// Area de mensagens ao usuario
	private JTextArea jtaMensagens;
	private JScrollPane jscMensagens;
	private jLabelFileStatus jlbStatus;
	private jLabelFileName jlbArquivo;
	// Area com as ferramentas
	private JToolBar jtoBarraBotoes;
	// Botoes
	private JButton btnNovo;
	private JButton btnAbrir;
	private JButton btnSalvar;
	// Botoes Editar
	private JButton btnCopiar;
	private JButton btnRecortar;
	private JButton btnColar;
	// Botoes de Acao
	private JButton btnCompilar;
	private JButton btnGerarCodigo;
	// Botoes de Ajuda
	private JButton btnSobre;

	// Caixa para criar novo arquivo
	private JFileChooser jfcNovo = new JFileChooser();

	// Variaveis auxiliares para controle
	private char fileStatus = 'N';
	// Valores possiveis
	// 'N' - Novo
	// 'S' - Salvo
	// 'A' - Aberto
	// 'M' - Modificado
	private File caminhoArquivoAberto = null;
	// controle dos Observadores
	private ArrayList<Listener> observadores = new ArrayList<Listener>();

	protected void initComponentes() throws IOException {
		super.setTitle("LFPA Editor");
		super.setBounds(0, 0, 800, 600);
		// Centraliza na tela
		super.setLocationRelativeTo(null);
		// Seta o tema
		this.tema(3);
		super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		// Cria os paineis
		this.painelHead = new JPanel();
		this.painelFoot = new JPanel();

		// Cria area de edicao do codigo
		this.jtaEditor = new JTextArea();
		// Scrool
		this.jscEditor = new JScrollPane(this.jtaEditor);
		// Cria os botoes
		this.btnNovo = new JButton("Novo", new ImageIcon(ImageIO.read( ClassLoader.getSystemResourceAsStream("icons/novo.png"))));
		// Posicao do texto fica alinhado de forma centralizada com a Imgem
		this.btnNovo.setVerticalTextPosition(AbstractButton.CENTER);
		// Imagem fica a direita
		this.btnNovo.setHorizontalTextPosition(AbstractButton.RIGHT);
		// Imagem fica sobre o texto
		this.btnNovo.setVerticalAlignment(AbstractButton.BOTTOM);
		// As bordas nao sao pintadas
		this.btnNovo.setBorderPainted(false);

		this.btnAbrir = new JButton("Abrir", new ImageIcon(ImageIO.read( ClassLoader.getSystemResourceAsStream("icons/abrir.png"))));
		
		this.btnAbrir.setVerticalTextPosition(AbstractButton.CENTER);
		this.btnAbrir.setHorizontalTextPosition(AbstractButton.RIGHT);
		this.btnAbrir.setVerticalAlignment(AbstractButton.BOTTOM);
		this.btnAbrir.setBorderPainted(false);

		this.btnSalvar = new JButton("Salvar",
				new ImageIcon(ImageIO.read( ClassLoader.getSystemResourceAsStream("icons/salvar.png"))));
		this.btnSalvar.setVerticalTextPosition(AbstractButton.CENTER);
		this.btnSalvar.setHorizontalTextPosition(AbstractButton.RIGHT);
		this.btnSalvar.setVerticalAlignment(AbstractButton.BOTTOM);
		this.btnSalvar.setBorderPainted(false);

		this.btnCopiar = new JButton("Copiar",
				new ImageIcon(ImageIO.read( ClassLoader.getSystemResourceAsStream("icons/copiar.png"))));
		this.btnCopiar.setVerticalTextPosition(AbstractButton.CENTER);
		this.btnCopiar.setHorizontalTextPosition(AbstractButton.RIGHT);
		this.btnCopiar.setVerticalAlignment(AbstractButton.BOTTOM);
		this.btnCopiar.setBorderPainted(false);

		this.btnRecortar = new JButton("Recortar",
				new ImageIcon(ImageIO.read( ClassLoader.getSystemResourceAsStream("icons/recortar.png"))));
		this.btnRecortar.setVerticalTextPosition(AbstractButton.CENTER);
		this.btnRecortar.setHorizontalTextPosition(AbstractButton.RIGHT);
		this.btnRecortar.setVerticalAlignment(AbstractButton.BOTTOM);
		this.btnRecortar.setBorderPainted(false);

		this.btnColar = new JButton("Colar", new ImageIcon(ImageIO.read( ClassLoader.getSystemResourceAsStream("icons/colar.png"))));
		this.btnColar.setVerticalTextPosition(AbstractButton.CENTER);
		this.btnColar.setHorizontalTextPosition(AbstractButton.RIGHT);
		this.btnColar.setVerticalAlignment(AbstractButton.BOTTOM);
		this.btnColar.setBorderPainted(false);

		this.btnCompilar = new JButton("Compilar", new ImageIcon(ImageIO.read( ClassLoader.getSystemResourceAsStream("icons/compilar.png"))));
		this.btnCompilar.setVerticalTextPosition(AbstractButton.CENTER);
		this.btnCompilar.setHorizontalTextPosition(AbstractButton.RIGHT);
		this.btnCompilar.setVerticalAlignment(AbstractButton.BOTTOM);
		this.btnCompilar.setBorderPainted(false);

		this.btnGerarCodigo = new JButton("Calcular",
				new ImageIcon(ImageIO.read( ClassLoader.getSystemResourceAsStream("icons/gerarCodigo.png"))));
		this.btnGerarCodigo.setVerticalTextPosition(AbstractButton.CENTER);
		this.btnGerarCodigo.setHorizontalTextPosition(AbstractButton.RIGHT);
		this.btnGerarCodigo.setVerticalAlignment(AbstractButton.BOTTOM);
		this.btnGerarCodigo.setBorderPainted(false);
		this.btnSobre = new JButton("Sobre",
				new ImageIcon(ImageIO.read( ClassLoader.getSystemResourceAsStream("icons/sobre.png"))));
		this.btnSobre.setVerticalTextPosition(AbstractButton.CENTER);
		this.btnSobre.setHorizontalTextPosition(AbstractButton.RIGHT);
		this.btnSobre.setVerticalAlignment(AbstractButton.BOTTOM);
		this.btnSobre.setBorderPainted(false);

		// Listeners dos Botoes da Barra de Tarefa
		this.btnNovo.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				novo();
			}
		});

		this.btnAbrir.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				abrir();
			}
		});

		this.btnSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				salvar();
			}
		});

		this.btnCopiar.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				// Copia o texto seleciona no editor de codigo
				jtaEditor.copy();
			}
		});

		this.btnColar.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				// Cola o texto no editor codigo
				jtaEditor.paste();
			}
		});

		this.btnRecortar.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				// Recorta o texto seleciona no editor de codigo
				jtaEditor.cut();
			}
		});

		this.btnGerarCodigo.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				gerarCodigo();
			}
		});

		this.btnCompilar.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				compilar();
			}
		});

		this.btnSobre.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				sobre();
			}
		});

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				if (fechar()) {
					setVisible(false);
					dispose();
					System.exit(0);
				} else {
					setVisible(true);
				}
			}
		});

		this.jtaEditor.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				jtaEditor.setSelectionStart(jtaEditor.getSelectionStart());
				jtaEditor.setSelectionEnd(jtaEditor.getSelectionEnd());
				jtaEditor.setSelectionColor(Color.lightGray);
				jtaEditor.setSelectedTextColor(Color.black);
				if (evt.getKeyCode() == KeyEvent.VK_N && evt.isControlDown())
					novo();
				else if (evt.getKeyCode() == KeyEvent.VK_A
						&& evt.isControlDown())
					abrir();
				else if (evt.getKeyCode() == KeyEvent.VK_S
						&& evt.isControlDown())
					salvar();
				else if (evt.getKeyCode() == KeyEvent.VK_Z
						&& evt.isControlDown())
					desfazer();
				else if (evt.getKeyCode() == KeyEvent.VK_Y
						&& evt.isControlDown())
					refazer();
				else if (evt.getKeyCode() == KeyEvent.VK_F9)
					compilar();
				else if (evt.getKeyCode() == KeyEvent.VK_F8)
					gerarCodigo();
				else if (evt.getKeyCode() == KeyEvent.VK_F1)
					sobre();
				else {
					fileStatus = 'M';
					update();
				}
			}
		});

		// Cria a barra dos botoes
		this.jtoBarraBotoes = new JToolBar();
		// Orientacao Vertical
		this.jtoBarraBotoes.setOrientation(JToolBar.VERTICAL);
		// Nao permite arrastar barra de tarefas
		this.jtoBarraBotoes.setFloatable(false);
		// Nao pinta a borda
		this.jtoBarraBotoes.setBorderPainted(false);
		// Alinhamento
		this.jtoBarraBotoes.setAlignmentX(this.jtoBarraBotoes.CENTER_ALIGNMENT);

		// Adiciona botoes e separadores
		this.jtoBarraBotoes.add(this.btnNovo);
		this.jtoBarraBotoes.add(this.btnAbrir);
		this.jtoBarraBotoes.add(this.btnSalvar);
		this.jtoBarraBotoes.add(new JSeparator());
		this.jtoBarraBotoes.add(this.btnCopiar);
		this.jtoBarraBotoes.add(this.btnRecortar);
		this.jtoBarraBotoes.add(this.btnColar);
		this.jtoBarraBotoes.add(new JSeparator());
		this.jtoBarraBotoes.add(this.btnCompilar);
		this.jtoBarraBotoes.add(this.btnGerarCodigo);
		this.jtoBarraBotoes.add(new JSeparator());
		this.jtoBarraBotoes.add(this.btnSobre);

		// Adiciona ao painel superior a barra de tarefas e o editor de codigo
		this.painelHead.add(this.jtoBarraBotoes);
		this.painelHead.add(this.jscEditor);

		// Painel auxiliar para garantir o tamanho fixo da Barra de Tarefas
		JPanel painelAux = new JPanel();
		GroupLayout painelAuxLayout = new GroupLayout(painelAux);
		painelAux.setLayout(painelAuxLayout);
		painelAuxLayout.setHorizontalGroup(painelAuxLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jtoBarraBotoes,
				GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE));
		painelAuxLayout.setVerticalGroup(painelAuxLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jtoBarraBotoes,
				GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE));

		GroupLayout painelHeadLayout = new GroupLayout(painelHead);
		painelHead.setLayout(painelHeadLayout);
		painelHeadLayout.setHorizontalGroup(painelHeadLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
						painelHeadLayout.createSequentialGroup().addComponent(
								painelAux, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE).addPreferredGap(
								LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jscEditor,
										GroupLayout.DEFAULT_SIZE, 563,
										Short.MAX_VALUE)));
		painelHeadLayout.setVerticalGroup(painelHeadLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(painelAux,
				GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
				GroupLayout.PREFERRED_SIZE).addComponent(jscEditor,
				GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE));

		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(painelHead,
				GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addComponent(painelHead,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE).addContainerGap(107,
						Short.MAX_VALUE)));

		// Foot
		this.jtaMensagens = new JTextArea();
		this.jtaMensagens.setEditable(false);
		this.jscMensagens = new JScrollPane(this.jtaMensagens);

		this.painelStatus = new JPanel();

		this.painelStatus.setBorder(BorderFactory.createEtchedBorder());
		this.painelStatus.setLayout(new GridLayout(1, 3));

		// criando labels da barra de status
		this.jlbStatus = new jLabelFileStatus("");
		this.jlbArquivo = new jLabelFileName("");
		// adicionando os labels como observadores
		this.observadores.add(this.jlbStatus);
		this.observadores.add(this.jlbArquivo);
		// adicionando os labels ao panel
		this.painelStatus.add(this.jlbStatus);
		this.painelStatus.add(this.jlbArquivo);
		// chuncho para deixar o espaçamento necessario
		this.painelStatus.add(new JLabel("                  "));

		// Cria o split adicionando o painel superior e inferior
		this.jspPaineis = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				this.painelHead, this.painelFoot);

		/*
		 * javax.swing.GroupLayout painelStatusLayout = new
		 * javax.swing.GroupLayout( painelStatus);
		 * painelStatus.setLayout(painelStatusLayout);
		 * painelStatusLayout.setHorizontalGroup(painelStatusLayout
		 * .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		 * .addGap(0, 253, Short.MAX_VALUE));
		 * painelStatusLayout.setVerticalGroup(painelStatusLayout
		 * .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		 * .addGap(0, 17, Short.MAX_VALUE));
		 */

		javax.swing.GroupLayout painelFootLayout = new javax.swing.GroupLayout(
				painelFoot);
		painelFoot.setLayout(painelFootLayout);
		painelFootLayout.setHorizontalGroup(painelFootLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(painelStatus,
						javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(jscMensagens,
						javax.swing.GroupLayout.DEFAULT_SIZE, 257,
						Short.MAX_VALUE));
		painelFootLayout.setVerticalGroup(painelFootLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				painelFootLayout.createSequentialGroup().addComponent(
						jscMensagens, javax.swing.GroupLayout.DEFAULT_SIZE,
						180, Short.MAX_VALUE).addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(painelStatus,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)));
		this.painelStatus = new JPanel();
		this.painelStatus.setBorder(BorderFactory.createEtchedBorder());

		// Adiciona o split a tela
		this.getContentPane().add(jspPaineis);

		// Configura Editor
		this.jtaEditor.setRows(10);
		this.lnb = new LineNumberedBorder(LineNumberedBorder.LEFT_SIDE,
				LineNumberedBorder.RIGHT_JUSTIFY);
		this.jtaEditor.setBorder(this.lnb);

	}

	public TelaGUI() throws IOException {
		this.controla = new Facade();
		this.controla.addOuvinte(this);
		this.initComponentes();
		this.jtaEditor.getDocument().addUndoableEditListener(
				new MyUndoableEditListener());

	}

	public void desfazer() {
		try {
			undo.undo();
		} catch (CannotUndoException ex) {
			System.out.println("Unable to undo: " + ex);
			ex.printStackTrace();
		}

	}

	public void refazer() {
		try {
			undo.redo();
		} catch (CannotRedoException ex) {
			System.out.println("Unable to redo: " + ex);
			ex.printStackTrace();
		}

	}

	protected class MyUndoableEditListener implements UndoableEditListener {
		public void undoableEditHappened(UndoableEditEvent e) {
			// Remember the edit and update the menus
			undo.addEdit(e.getEdit());

		}
	}

	private boolean fileManager(boolean checkModify) {
		// verifica se o arquivo esta modificado
		if (this.fileStatus == 'M') {
			// verifica se é para verificar alterações no arquivo
			if (checkModify) {
				int i = JOptionPane
						.showConfirmDialog(
								null,
								"Deseja salvar as alterações antes de prosseguir?",
								"Salvar", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (i == JOptionPane.NO_OPTION)
					// Clicou em não, ou seja, nao quer salvar
					return true;
				else if (i == JOptionPane.CANCEL_OPTION)
					// Clicou em cancel, nao pode continuar
					return false;
			}
			// Se for um arquivo novo
			if (this.caminhoArquivoAberto == null) {
				// pede onde salvar
				int j = jfcNovo.showSaveDialog(null);
				if (j == JFileChooser.APPROVE_OPTION) {
					this.caminhoArquivoAberto = jfcNovo.getSelectedFile();
				} else {
					return false;
				}
			}
			// Realiza a persistencia e salva
			Persistencia.getInstancia().gravarArquivoFonte(
					jtaEditor.getText().trim(),
					this.caminhoArquivoAberto.getAbsolutePath());

			return true;
		} else
			return true;
	}

	/* Criando um novo arquivo */
	private void novo() {
		fechar();
		this.fileStatus = 'N';
		caminhoArquivoAberto = null;
		this.jtaEditor.setText("");
		this.update();
	}

	/* Abrindo um novo arquivo */
	private void abrir() {
		// Gerencia as modificações
		if (fileManager(true)) {
			// escolha para abrir outro
			final int i = jfcNovo.showOpenDialog(null);
			if (i == JFileChooser.APPROVE_OPTION) {
				jtaEditor.setText(Persistencia.getInstancia().lerArquivoFonte(
						jfcNovo.getSelectedFile().getAbsolutePath()));
				this.caminhoArquivoAberto = jfcNovo.getSelectedFile();
				this.fileStatus = 'A';
				this.update();
			}
		}
	}

	/* salvando o arquivo */
	private void salvar() {
		// Chama método que trabalha com as situações do arquivo
		if (fileManager(false)) {
			this.fileStatus = 'S';
			this.update();
		}
	}

	/* fecha o arquivo aberto */
	private boolean fechar() {
		if (fileManager(true)) {
			this.fileStatus = 'N';
			Persistencia.getInstancia().fechaArquivoCorrente();
			this.update();
			return true;
		} else
			return false;
	}

	private boolean compilar() {
		// limpa a area de mensagens
		this.jtaMensagens.setForeground(Color.GREEN);
		
		this.jtaMensagens.setText("Iniciando compilacao...\n");
		
		boolean r = this.controla.analisa(this.jtaEditor.getText().trim());
		this.jtaEditor.requestFocus(true);
		return r;
	}

	private void gerarCodigo() {
		if (!this.compilar())
			return;
		Resultado r = ControlaCalc.getInstance().getResultado(ControlaCalc.getInstance().getProjetoAtual());
		this.jtaMensagens.setForeground(Color.BLUE);
		if ( ControlaCalc.getInstance().getProjeto(ControlaCalc.getInstance().getProjetoAtual()).getTipo() == Constants.DESENVOLVIMENTO)
		this.jtaMensagens.append("\nProjeto de Desenvolvimento: \n\t" +
                "* Fator Ajuste...= " +
                   CalculoFPA.getInstance().calcFatorAjuste(r.getTotalNC()) + "\n\t" +
                "* FPA Ajustados..= " + 
                   CalculoFPA.getInstance().calcPFAjustados(r.getTotalFPA(),CalculoFPA.getInstance().calcFatorAjuste(r.getTotalNC()))  + "\n\t" +
                "* Custo Sistema..= " +   CalculoFPA.getInstance().calcCustoSistema(CalculoFPA.getInstance().calcPFAjustados(r.getTotalFPA(),CalculoFPA.getInstance().calcFatorAjuste(r.getTotalNC())), 1, 8)
                   );
		else
			this.jtaMensagens.append("\nProjeto de Melhoria.......: " +
					CalculoFPA.getInstance().calcProjetoMelhoria(r.getADD(), r.getCHGA(), r.getDEL(), r.getCFP(), r.getVAFA(), r.getVAFB()));
		
		this.jtaEditor.requestFocus(true);
	}

	private void sobre() {
		JOptionPane.showMessageDialog(null, "LFPA:\n"
				+ " * Version 1.0.0 ", "Sobre",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/* Muda o tema conforme o parametro passado */
	private void tema(int i) {
		LookAndFeelInfo[] looks = javax.swing.UIManager
				.getInstalledLookAndFeels();
		try {
			javax.swing.UIManager.setLookAndFeel(looks[i].getClassName());
			javax.swing.SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* avisa os observadores que deve haver atualização */
	private void update() {
		// varre os listeners
		for (Listener l : this.observadores)
			l.update();
	}

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new TelaGUI().setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void emiteMensagemCompilacao(String msg) {
		JOptionPane.showMessageDialog(null, msg.trim(),
				"Mensagem da Compilacao", JOptionPane.INFORMATION_MESSAGE);
	}

	public void exibeDadosDeSaida(String msg,boolean analisouComSucesso) {
		if (analisouComSucesso)
			this.jtaMensagens.setForeground(Color.GREEN);
		else 
			this.jtaMensagens.setForeground(Color.RED);
		
		this.jtaMensagens.append(msg);

	}

	public void setSelecaoErroNoEditor(int ini, int end, int TIPO) {
		final String auxIni = this.jtaEditor.getText().substring(0, ini);
		final String auxFim = this.jtaEditor.getText().substring(0,
				this.jtaEditor.getText().length() - 1);

		int i = ini-1;
		int j = ini;
		for (; auxIni.charAt(i) != '\n' && i > 0; i--);
		for (; auxFim.charAt(j) != '\n'
				&& j < this.jtaEditor.getText().length() - 1; j++)
			;

		this.jtaEditor.setSelectionStart(i);
		this.jtaEditor.setSelectionEnd(j);
		this.jtaEditor.setSelectionColor(Color.red);
		this.jtaEditor.setSelectedTextColor(Color.yellow);

	}

	public void exibeDadosDeSaida(String msg) {
		this.jtaMensagens.append(msg);
		
	}
}
